# Sistema Inteligente de Classificação de Ocorrências — Backend

API REST em Java + Spring Boot que recebe descrições informais de ocorrências, aciona uma LLM para formalizar, classificar e sugerir ações, e persiste o resultado para consulta futura.

## Sumário

- [1. Problema Resolvido](#1-problema-resolvido)
- [2. Arquitetura](#2-arquitetura)
- [3. Fluxo da Aplicação](#3-fluxo-da-aplicação)
- [4. Tecnologias](#4-tecnologias)
- [5. Estrutura de Pastas](#5-estrutura-de-pastas)
- [6. Endpoints](#6-endpoints)
- [7. Como Executar Localmente](#7-como-executar-localmente)
  - [7.1. Pré-requisitos](#71-pré-requisitos)
  - [7.2. Passos](#72-passos)
  - [7.3. Configuração (`config.properties`)](#73-configuração-configproperties)
  - [7.4. Banco de Dados H2](#74-banco-de-dados-h2)
  - [7.5. Testes](#75-testes)
- [8. Categorias de Incidente](#8-categorias-de-incidente)
- [9. Limitações Conhecidas](#9-limitações-conhecidas)
- [10. Melhorias Futuras](#10-melhorias-futuras)

---

## 1. Problema Resolvido

Operadores de portaria registram ocorrências de forma informal e sem padrão, dificultando auditoria e acompanhamento.
O sistema usa IA para transformar esses relatos em registros formais, padronizados, categorizados e com sugestões de ação.

**Exemplo:**
Entrada: `"cara tentou entrar sem cadastro e ficou insistindo"`
Saída: `"Visitante sem cadastro prévio tentou acessar as dependências sem autorização. Após orientação da equipe de portaria, o acesso foi negado. O indivíduo apresentou comportamento insistente, sendo registrado para auditoria."`

---

## 2. Arquitetura

A aplicação segue uma arquitetura em camadas simples, com baixo acoplamento entre os módulos:

```
Controller  →  Service  →  ApiProvider (interface)
                    ↓               ↓
             Repository      [Anthropic | OpenAI | Gemini]
                    ↓
              H2 Database
```

- **Controller**: recebe e valida a requisição HTTP
- **Service**: orquestra a chamada à IA, trata a resposta e persiste o registro
- **ApiProvider**: interface que abstrai o provedor de LLM; cada provider é uma implementação independente selecionada via configuração
- **Repository**: persistência via Spring Data JPA

---

## 3. Fluxo da Aplicação

```
1. Operador envia POST /incidentClassifier com o texto da ocorrência
2. Controller valida o campo obrigatório (não pode ser vazio)
3. Service envia o system prompt + texto do operador para a LLM configurada
4. LLM retorna um JSON estruturado com: texto formalizado, categoria, gravidade e ação sugerida
5. Service desserializa e valida o JSON retornado
6. Registro é persistido no banco H2 (com texto original + classificação)
7. Resposta é retornada ao operador
```

---

## 4. Tecnologias

| Tecnologia        | Versão | Uso                                     |
| ----------------- | ------ | --------------------------------------- |
| Java              | 25     | Linguagem principal                     |
| Spring Boot       | 3.4.5  | Framework web e injeção de dependências |
| Spring Data JPA   | —      | Persistência de dados                   |
| H2 Database       | —      | Banco embarcado file-based              |
| Spring Validation | —      | Validação de entrada e saída            |
| Lombok            | —      | Redução de boilerplate                  |
| OpenAI SDK        | 4.36.0 | Integração com GPT                      |
| Google GenAI SDK  | 1.16.0 | Integração com Gemini                   |
| Anthropic         | —      | Integração via HTTP nativo              |
| Maven             | —      | Build e dependências                    |

---

## 5. Estrutura de Pastas

```
backend/
└── src/main/java/br/gov/sctec/incidentclassifier/
    ├── IncidentClassifierApplication.java              # Ponto de entrada Spring Boot
    ├── config/
    │   ├── SystemPromptConfig.java                      # Carrega system prompt do arquivo
    │   ├── ToolDefinitions.java                         # Define ferramentas/tokens para providers
    │   └── WebConfig.java                               # Configuração CORS/MVC
    ├── controller/
    │   └── IncidentClassifierController.java           # Endpoint REST
    ├── dto/
    │   ├── ErrorResponse.java                           # Modelo de resposta de erro
    │   └── IncidentRequest.java                         # Body do POST /incidentClassifier
    ├── exception/
    │   ├── GlobalExceptionHandler.java                  # Tratamento global de exceções
    │   └── IncidentClassificationException.java         # Exceção de domínio
    ├── model/
    │   ├── IncidentCategory.java                        # Enum de categorias
    │   ├── IncidentClassification.java                   # DTO de classificação da IA
    │   ├── IncidentRecord.java                           # Entidade JPA persistida
    │   ├── IncidentSeverity.java                         # Enum de gravidade
    │   └── Tool.java                                     # Modelo de tool para providers
    ├── provider/
    │   ├── ApiProvider.java                              # Interface de providers de IA
    │   ├── ApiProviderConfig.java                        # Configuração de beans ativa
    │   ├── anthropic/
    │   │   └── AnthropicApiProvider.java                 # Implementação Anthropic
    │   ├── gemini/
    │   │   └── GeminiApiProvider.java                    # Implementação Gemini
    │   ├── mock/
    │   │   └── MockApiProvider.java                      # Provider de teste/mock
    │   └── openai/
    │       └── OpenAiApiProvider.java                    # Implementação OpenAI
    ├── repository/
    │   └── IncidentRepository.java                      # Spring Data JPA repository
    └── service/
        ├── IncidentClassifierService.java               # Orquestra IA e persistência
        └── ToolExecutor.java                             # Executa ferramentas auxiliares

backend/src/main/resources/
├── application.properties                              # Configuração Spring Boot
├── config.properties                                   # Credenciais e provider ativo
└── prompts/
    └── incident-classifier-system-prompt.txt          # System prompt da LLM

backend/src/test/java/br/gov/sctec/incidentclassifier/
├── IncidentClassifierApplicationTest.java              # Teste de inicialização do contexto
├── controller/
│   └── IncidentClassifierControllerTest.java           # Testes do controlador
├── dto/
│   ├── ErrorResponseTest.java                          # Teste do DTO de erro
│   └── IncidentRequestTest.java                        # Teste do request DTO
├── exception/
│   └── IncidentClassificationExceptionTest.java        # Teste de exceção de domínio
├── model/
│   ├── IncidentCategoryTest.java                       # Teste do enum de categoria
│   ├── IncidentClassificationTest.java                 # Teste do DTO de classificação
│   ├── IncidentRecordTest.java                         # Teste do registro persistido
│   └── IncidentSeverityTest.java                       # Teste do enum de gravidade
├── repository/
│   └── IncidentRepositoryTest.java                     # Teste do repositório
└── service/
    └── IncidentClassifierServiceTest.java              # Teste do serviço

backend/src/test/resources/
└── application.properties                              # Propriedades de teste
```

---

## 6. Endpoints

**`POST /incidentClassifier` — Classificar ocorrência**

**Request:**

```bash
curl -s -X POST "http://localhost:8080/incidentClassifier" \
  -H "Content-Type: application/json" \
  -d "{\"incident\":\"Sistema indisponível para todos os usuários desde as 08:00\"}"
```

**Response `200 OK`:**

```json
{
  "formalizedIncidentText": "Sistema crítico apresenta indisponibilidade total para todos os usuários desde as 08:00h, comprometendo as operações da empresa.",
  "category": "SERVICE_OUTAGE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T18:00:00",
  "suggestedAction": "Acionar imediatamente a equipe de infraestrutura e abrir chamado de alta prioridade. Comunicar gestores sobre o impacto operacional."
}
```

---

**`GET /incidentClassifier` — Listar histórico**

**Request:**

```bash
curl -s -X GET "http://localhost:8080/incidentClassifier"
```

**Response `200 OK`:**

```json
[
  {
    "id": 1,
    "originalText": "Sistema indisponível para todos os usuários desde as 08:00",
    "formalizedIncidentText": "Sistema crítico apresenta indisponibilidade total...",
    "category": "SERVICE_OUTAGE",
    "severity": "HIGH",
    "registeredAt": "2026-05-19T18:00:00",
    "suggestedAction": "Acionar imediatamente a equipe de infraestrutura..."
  }
]
```

---

**Resposta de erro `422 Unprocessable Entity`:**

```json
{
  "status": 422,
  "error": "Incident Classification Error",
  "message": "Failed to parse AI response into IncidentClassification",
  "timestamp": "2026-05-19T18:00:00"
}
```

---

## 7. Como Executar Localmente

### 7.1. Pré-requisitos

- Java 25+
- Maven 3.9+
- Chave de API de um dos providers: OpenAI, Google Gemini ou Anthropic

### 7.2. Passos

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/incident-classifier.git
cd incident-classifier/backend

# 2. Configure as credenciais
# Edite src/main/resources/config.properties

# 3. Execute
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

### 7.3. Configuração (`config.properties`)

```properties
# Selecione o provider ativo: OPENAI, GEMINI ou ANTHROPIC
ai.provider=OPENAI

# OpenAI
ai.openai.api-key=sk-...
ai.openai.model=gpt-4o-mini
ai.openai.max-tokens=4096

# Google Gemini
ai.gemini.api-key=AIza...
ai.gemini.model=gemini-2.0-flash-lite
ai.gemini.max-output-tokens=4096

# Anthropic
ai.anthropic.api-key=sk-ant-...
ai.anthropic.model=claude-haiku-3-5
ai.anthropic.max-tokens=4096
```

> ⚠️ Nunca versione o `config.properties` com chaves reais. Adicione ao `.gitignore`.

---

### 7.4. Banco de Dados H2

O banco é criado automaticamente na pasta `backend/data/` ao iniciar a aplicação.

**Console web:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

| Campo     | Valor                                     |
| --------- | ----------------------------------------- |
| JDBC URL  | `jdbc:h2:file:./data/incident-classifier` |
| User Name | `sa`                                      |
| Password  | _(em branco)_                             |

---

### 7.5. Testes

```bash
./mvnw test
```

O projeto possui teste de contexto Spring Boot (`contextLoads`) que valida a inicialização correta da aplicação.

---

## 8. Categorias de Incidente

| Categoria        | Descrição                                                |
| ---------------- | -------------------------------------------------------- |
| `SECURITY`       | Violações de segurança, acessos não autorizados, malware |
| `NETWORK`        | Falhas de conectividade e rede                           |
| `HARDWARE`       | Falhas físicas de equipamentos                           |
| `SOFTWARE`       | Erros de aplicação, bugs, falhas de deploy               |
| `DATABASE`       | Indisponibilidade ou corrupção de banco de dados         |
| `DATA_LOSS`      | Perda ou exclusão acidental de dados                     |
| `ACCESS_CONTROL` | Problemas de autenticação e permissões                   |
| `PERFORMANCE`    | Lentidão, alta latência, esgotamento de recursos         |
| `SERVICE_OUTAGE` | Indisponibilidade total de serviço crítico               |
| `INFRASTRUCTURE` | Falhas de servidor, cloud ou virtualização               |
| `COMMUNICATION`  | Falhas em e-mail, mensageria ou videoconferência         |
| `COMPLIANCE`     | Violações regulatórias ou de política interna            |
| `OTHER`          | Incidentes que não se enquadram nas demais categorias    |

---

## 9. Limitações Conhecidas

- Não há autenticação nos endpoints (fora do escopo do MVP)
- O banco H2 é embarcado — não adequado para ambientes com múltiplas instâncias
- A qualidade da classificação depende do provider de IA configurado e do crédito disponível
- Não há paginação no endpoint de listagem do histórico

---

## 10. Melhorias Futuras

- Implementar autenticação
- Implementar funcionalidade de edição e exclusão de ocorrência
- Adicionar paginação e filtros no endpoint de histórico
- Implementar busca semântica por ocorrências similares
- Adicionar LLM-as-a-Judge para validar a coerência da classificação
- Migrar para banco relacional externo (PostgreSQL)
