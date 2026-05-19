# Copilot Instructions — Incident Classifier

## Visão Geral do Projeto

Sistema de registro e classificação de incidentes corporativos com auxílio de IA.
O operador descreve uma ocorrência em linguagem natural; a IA formaliza o texto, categoriza o incidente e define a gravidade.

Repositório monorepo com dois módulos:
- `backend/` — API REST Java + Spring Boot
- `frontend/` — Interface web (a implementar)
- `docs/` — Documentação (user stories, prompts, fluxograma)

---

## Módulo Backend

### Stack
- **Java 25**
- **Maven** (build e gerenciamento de dependências)
- **Spring Boot 3.4.5** (`spring-boot-starter-parent`)
- **Spring Web** (`spring-boot-starter-web`)
- **Spring Validation** (`spring-boot-starter-validation`)
- **Lombok**
- **Jackson** (serialização/deserialização JSON)
- **SDK OpenAI** (`com.openai:openai-java:4.36.0`)
- **SDK Google Gemini** (`com.google.genai:google-genai:1.16.0`)
- **Anthropic** (integração via HTTP client nativo)
- **Spring Data JPA** (`spring-boot-starter-data-jpa`)
- **H2 Database** (banco embarcado file-based para persistência)

### Coordenadas Maven
```xml
<groupId>br.gov.sctec</groupId>
<artifactId>incident-classifier</artifactId>
<version>0.1.0</version>
```

### Configuração
- Porta: `8080`
- Propriedades da aplicação: `src/main/resources/application.properties`
- Propriedades de configuração sensível: `src/main/resources/config.properties`
- Provider de IA ativo: propriedade `ai.provider` em `config.properties` (valores: `ANTHROPIC`, `OPENAI`, `GEMINI`)

---

## Estrutura de Pacotes

Pacote raiz: `br.gov.sctec.incidentclassifier`

```
br.gov.sctec.incidentclassifier
├── IncidentClassifierApplication.java       # Classe principal Spring Boot
├── config/
│   └── SystemPromptConfig.java              # Carrega o system prompt do arquivo .txt
├── controller/
│   └── IncidentClassifierController.java    # POST /incidentClassifier
├── dto/
│   ├── IncidentRequest.java                 # Request body: { "incident": "..." }
│   └── ErrorResponse.java                   # Resposta de erro padronizada
├── exception/
│   ├── GlobalExceptionHandler.java          # @ControllerAdvice com tratamento global
│   └── IncidentClassificationException.java # Exceção de domínio do classificador
├── model/
│   ├── IncidentClassification.java          # Resultado da classificação (retorno da API)
│   ├── IncidentRecord.java                  # Entidade JPA persistida no banco
│   ├── IncidentCategory.java                # Enum com categorias de incidente
│   ├── IncidentSeverity.java                # Enum: LOW, MEDIUM, HIGH
│   └── Tool.java                            # Modelo de ferramenta para providers de IA
├── repository/
│   └── IncidentRepository.java              # Spring Data JPA — CRUD de IncidentRecord
├── provider/
│   ├── ApiProvider.java                     # Interface: getResponse(systemPrompt, userPrompt, tools)
│   ├── ApiProviderConfig.java               # @Configuration que injeta o provider correto via config
│   ├── anthropic/
│   │   └── AnthropicApiProvider.java        # Implementação Anthropic (HTTP nativo)
│   ├── gemini/
│   │   └── GeminiApiProvider.java           # Implementação Google Gemini
│   └── openai/
│       └── OpenAiApiProvider.java           # Implementação OpenAI
└── service/
    └── IncidentClassifierService.java        # Orquestra a chamada ao provider e deserializa o JSON
```

---

## Modelos de Domínio

### IncidentClassification
```java
String formalizedIncidentText  // texto formalizado pela IA (@NotBlank)
IncidentCategory category       // categoria do incidente (@NotNull)
IncidentSeverity severity       // gravidade (@NotNull)
LocalDateTime registeredAt      // data/hora do registro (@NotNull)
String suggestedAction          // ação recomendada pela IA (@NotBlank)
```

### IncidentRecord (entidade JPA — tabela `incident_records`)
```java
Long id                         // chave primária auto-gerada
String originalText             // texto original do operador
String formalizedIncidentText   // texto formalizado pela IA
IncidentCategory category       // categoria
IncidentSeverity severity       // gravidade
LocalDateTime registeredAt      // data/hora do registro
String suggestedAction          // ação recomendada
```

### IncidentCategory (enum)
`SECURITY`, `NETWORK`, `HARDWARE`, `SOFTWARE`, `DATABASE`, `DATA_LOSS`,
`ACCESS_CONTROL`, `PERFORMANCE`, `SERVICE_OUTAGE`, `INFRASTRUCTURE`,
`COMMUNICATION`, `COMPLIANCE`, `OTHER`

### IncidentSeverity (enum)
`LOW`, `MEDIUM`, `HIGH`

---

## Arquitetura

### Fluxo da requisição
```
POST /incidentClassifier
  → IncidentClassifierController
  → IncidentClassifierService.incidentClassifier(userPrompt)
  → ApiProvider.getResponse(systemPrompt, userPrompt, tools)
  → [Anthropic | OpenAI | Gemini]
  → deserializa JSON → IncidentClassification
  → IncidentRepository.save(IncidentRecord)
  → ResponseEntity<IncidentClassification>

GET /incidentClassifier
  → IncidentClassifierController
  → IncidentClassifierService.listIncidents()
  → IncidentRepository.findAll()
  → ResponseEntity<List<IncidentRecord>>
```

### Seleção do Provider
`ApiProviderConfig` lê a propriedade `ai.provider` e injeta o `@Bean ApiProvider` correto.
Cada provider implementa a interface `ApiProvider` com o método:
```java
String getResponse(String systemPrompt, String userPrompt, List<Tool> tools)
```

### System Prompt
Carregado na inicialização pela classe `SystemPromptConfig` a partir de:
`src/main/resources/prompts/incident-classifier-system-prompt.txt`
Injetado como `@Bean @Qualifier("incidentClassifierSystemPrompt") String`.

---

## Endpoints da API

| Método | Path | Request Body | Response |
|--------|------|--------------|----------|
| `POST` | `/incidentClassifier` | `{ "incident": "texto livre" }` | `IncidentClassification` (JSON) |
| `GET` | `/incidentClassifier` | — | `List<IncidentRecord>` (JSON) |

### Exemplos de uso (curl)

**Classificar incidente:**
```bash
curl -s -X POST "http://localhost:8080/incidentClassifier" \
  -H "Content-Type: application/json" \
  -d "{\"incident\":\"Sistema indisponível para todos os usuários desde as 08:00\"}"
```

**Listar histórico:**
```bash
curl -s -X GET "http://localhost:8080/incidentClassifier"
```

---

## Banco de Dados H2

- **Arquivo:** `backend/data/incident-classifier.mv.db` (criado automaticamente ao iniciar)
- **Console web:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:file:./data/incident-classifier`
- **User Name:** `sa`
- **Password:** *(deixar em branco)*
- **Tabela principal:** `INCIDENT_RECORDS`

---

## Convenções de Código

- Nomenclatura de métodos, variáveis e classes **em inglês**
- Seguir padrão Java (camelCase para métodos/variáveis, PascalCase para classes)
- Usar **Lombok** (`@Data`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`, etc.)
- Usar **`log.info`/`log.error`** via `@Slf4j` para logging
- Validações com **Jakarta Validation** (`@NotBlank`, `@NotNull`)
- Tratamento de exceções centralizado no `GlobalExceptionHandler`
- Não adicionar comentários desnecessários no código
- Não criar métodos auxiliares para operações que ocorrem uma única vez

---

## O que já está implementado

- [x] Estrutura do projeto Maven + Spring Boot
- [x] Interface `ApiProvider` com implementações para Anthropic, OpenAI e Gemini
- [x] Seleção de provider via `config.properties`
- [x] Modelos: `IncidentClassification`, `IncidentRecord`, `IncidentCategory`, `IncidentSeverity`
- [x] System prompt carregado do arquivo `.txt`
- [x] `IncidentClassifierService` — orquestra a chamada, persiste e deserializa o retorno JSON
- [x] `IncidentClassifierController` — endpoints `POST` e `GET /incidentClassifier`
- [x] `IncidentRepository` — persistência com Spring Data JPA + H2
- [x] Tratamento global de exceções
- [x] Estrutura monorepo (`backend/`, `frontend/`, `docs/`)
- [x] **US-02** — Persistência de ocorrências com H2
- [x] **US-06** — Sugestão de ação retornada pela IA (`suggestedAction`)
- [x] **US-08** — Listagem do histórico de ocorrências

## O que falta implementar (baseado nas User Stories)

- [ ] **US-07** — Frontend com visualização estruturada do resultado

Consulte o arquivo `docs/user-stories.md` para os critérios de aceite completos.

---

## Regras para Geração de Código

1. **Sempre** seguir a estrutura de pacotes existente
2. Novas integrações com provedores de IA devem implementar a interface `ApiProvider`
3. Novos campos no retorno da IA devem ser adicionados em `IncidentClassification` e refletidos no system prompt
4. Configurações sensíveis (API keys, URLs) vão em `config.properties`, nunca em `application.properties`
5. Não criar dependências cíclicas entre pacotes
6. Não usar `@Autowired` por campo — preferir injeção via construtor com `@RequiredArgsConstructor`
