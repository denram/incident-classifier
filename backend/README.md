# incident-classifier — Backend

API REST em Spring Boot para classificação de incidentes via IA.

## Pré-requisitos

- **Java 25+** — verifique com `java -version`
- **Maven 3.9+** — verifique com `mvn -version`

## Configuração

Crie o arquivo `src/main/resources/config.properties` com as credenciais do provedor de IA escolhido.
Este arquivo está no `.gitignore` e **nunca deve ser commitado**.

### Opção 1 — Anthropic (Claude)

```properties
ai.provider=anthropic
ai.anthropic.api-key=sk-ant-...
ai.anthropic.model=claude-sonnet-4-6
ai.anthropic.api-url=https://api.anthropic.com/v1/messages
ai.anthropic.version=2023-06-01
ai.anthropic.max-tokens=1024
```

### Opção 2 — OpenAI

```properties
ai.provider=openai
ai.openai.api-key=sk-...
ai.openai.model=gpt-4o
ai.openai.max-tokens=1024
```

### Opção 3 — Google Gemini

```properties
ai.provider=gemini
ai.gemini.api-key=AIza...
ai.gemini.model=gemini-2.0-flash
ai.gemini.max-output-tokens=1024
```

## Subindo o servidor

```bash
cd backend
mvn spring-boot:run
```

O servidor inicia em `http://localhost:8080`.

Para build e execução via JAR:

```bash
mvn clean package
java -jar target/incident-classifier-0.1.0.jar
```

## Endpoints

### `POST /incidentClassifier`
Classifica um incidente usando IA.

**Body:**
```json
{ "incident": "Servidor de produção fora do ar desde as 14h." }
```

**Resposta:**
```json
{
  "formalizedIncidentText": "Indisponibilidade total do servidor de produção...",
  "category": "SERVICE_OUTAGE",
  "severity": "HIGH",
  "registeredAt": "2026-05-20T18:00:00",
  "suggestedAction": "Acionar equipe de infraestrutura imediatamente."
}
```

### `GET /incidentClassifier`
Retorna o histórico de todos os incidentes classificados.

## Banco de dados

Usa H2 (arquivo local em `./data/incident-classifier`). O console web está disponível em:

```
http://localhost:8080/h2-console
```

- **JDBC URL:** `jdbc:h2:file:./data/incident-classifier`
- **Usuário:** `sa`
- **Senha:** *(vazio)*

## Categorias de incidente

`SECURITY`, `NETWORK`, `HARDWARE`, `SOFTWARE`, `DATABASE`, `DATA_LOSS`,
`ACCESS_CONTROL`, `PERFORMANCE`, `SERVICE_OUTAGE`, `INFRASTRUCTURE`,
`COMMUNICATION`, `COMPLIANCE`, `OTHER`

## Severidades

`LOW`, `MEDIUM`, `HIGH`
