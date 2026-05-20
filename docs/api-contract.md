# Contrato de API REST
## Sistema Inteligente de Registro e Classificação de Ocorrências (SICO)

**Versão:** 2.0 (revisado para refletir implementação real do backend)
**Data:** Maio 2026
**Status:** Especificação Validada contra Código
**Classificação:** Interno

---

## 1. Introdução

### 1.1 Objetivo
Este documento especifica o contrato de API REST do SICO com base na implementação real do backend Spring Boot 3.4.5. Todos os campos, tipos, status codes e formatos de resposta foram validados contra o código-fonte.

### 1.2 Padrões

| Convenção | Valor |
|-----------|-------|
| **Base URL** | `http://localhost:8080` (dev) |
| **Content-Type** | `application/json` |
| **Charset** | `UTF-8` |
| **Timestamps** | ISO 8601 sem timezone (ex.: `2026-05-19T14:35:22`) |
| **Autenticação** | Não implementada — sem header Authorization |

> **Nota:** Não existe prefixo `/api`. Os endpoints começam em `/incidentClassifier`.

---

## 2. DTOs (Data Transfer Objects)

### 2.1 IncidentRequest — Request DTO

```typescript
interface IncidentRequest {
  incident: string; // @NotBlank — obrigatório, sem limite de tamanho no backend
}
```

**Exemplo válido:**
```json
{
  "incident": "Sistema indisponível para todos os usuários desde as 08:00. Banco de dados não responde."
}
```

**Exemplo inválido — campo vazio:**
```json
{
  "incident": ""
}
```

---

### 2.2 IncidentClassification — Response DTO (POST)

```typescript
interface IncidentClassification {
  formalizedIncidentText: string;   // @NotBlank
  category: IncidentCategory;       // @NotNull, enum
  severity: IncidentSeverity;       // @NotNull, enum
  registeredAt: string;             // @NotNull, ISO 8601 sem timezone
  suggestedAction: string;          // @NotBlank
}

enum IncidentCategory {
  SECURITY        = "SECURITY",
  NETWORK         = "NETWORK",
  HARDWARE        = "HARDWARE",
  SOFTWARE        = "SOFTWARE",
  DATABASE        = "DATABASE",
  DATA_LOSS       = "DATA_LOSS",
  ACCESS_CONTROL  = "ACCESS_CONTROL",
  PERFORMANCE     = "PERFORMANCE",
  SERVICE_OUTAGE  = "SERVICE_OUTAGE",
  INFRASTRUCTURE  = "INFRASTRUCTURE",
  COMMUNICATION   = "COMMUNICATION",
  COMPLIANCE      = "COMPLIANCE",
  OTHER           = "OTHER"
}

enum IncidentSeverity {
  LOW    = "LOW",
  MEDIUM = "MEDIUM",
  HIGH   = "HIGH"
}
```

**Exemplo de resposta:**
```json
{
  "formalizedIncidentText": "O banco de dados principal está indisponível, afetando todos os usuários.",
  "category": "DATABASE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T14:35:22",
  "suggestedAction": "Verificar logs do DBMS, validar espaço em disco, contatar DBA."
}
```

---

### 2.3 IncidentRecord — Response DTO (GET lista)

```typescript
interface IncidentRecord {
  id: number;                       // Long auto-incrementado (1, 2, 3...)
  originalText: string;             // Texto original digitado pelo operador
  formalizedIncidentText: string;   // Texto normalizado pela IA
  category: IncidentCategory;
  severity: IncidentSeverity;
  registeredAt: string;             // ISO 8601 sem timezone
  suggestedAction: string;
}
```

**Exemplo:**
```json
{
  "id": 1,
  "originalText": "Sistema fora do ar desde as 08:00",
  "formalizedIncidentText": "O banco de dados principal está indisponível...",
  "category": "DATABASE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T14:35:22",
  "suggestedAction": "Verificar logs do DBMS..."
}
```

---

### 2.4 ErrorResponse — DTO de Erro

```typescript
interface ErrorResponse {
  status: number;       // HTTP status code (400, 422, 500)
  error: string;        // Tipo do erro (ex.: "Validation Error")
  message: string;      // Mensagem detalhada (pode concatenar múltiplos campos)
  timestamp: string;    // ISO 8601 sem timezone
}
```

**Exemplo — validação:**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "incident: Incident description must not be blank",
  "timestamp": "2026-05-19T14:30:00"
}
```

**Exemplo — erro de classificação (IA falhou):**
```json
{
  "status": 422,
  "error": "Incident Classification Error",
  "message": "Failed to parse AI response",
  "timestamp": "2026-05-19T14:30:00"
}
```

**Exemplo — erro genérico:**
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected error occurred",
  "timestamp": "2026-05-19T14:30:00"
}
```

---

## 3. Endpoints

### 3.1 POST /incidentClassifier — Classificar Incidente

**Descrição:** Envia o texto do incidente para a IA, classifica e persiste no banco H2.

| Campo | Valor |
|-------|-------|
| Método | `POST` |
| URL | `/incidentClassifier` |
| Content-Type | `application/json` |
| Autenticação | Não requerida |
| Timeout esperado | Até 30 s (inclui chamada à IA) |

#### Request

```http
POST /incidentClassifier HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "incident": "Sistema indisponível para todos os usuários desde as 08:00. Banco de dados não responde."
}
```

#### Response — 200 OK (Sucesso)

> ⚠️ Retorna **200 OK** (não 201) e o objeto `IncidentClassification` **diretamente**, sem wrapper.

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "formalizedIncidentText": "O banco de dados principal está indisponível, afetando todos os usuários.",
  "category": "DATABASE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T14:35:22",
  "suggestedAction": "Verificar logs do DBMS, validar espaço em disco, executar integridade de índices, contatar DBA."
}
```

#### Response — 400 Bad Request (Campo vazio)

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "status": 400,
  "error": "Validation Error",
  "message": "incident: Incident description must not be blank",
  "timestamp": "2026-05-19T14:30:00"
}
```

#### Response — 422 Unprocessable Entity (IA falhou ao classificar)

```http
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/json

{
  "status": 422,
  "error": "Incident Classification Error",
  "message": "Failed to parse classification response from AI provider",
  "timestamp": "2026-05-19T14:30:00"
}
```

#### Response — 500 Internal Server Error

```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/json

{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected error occurred",
  "timestamp": "2026-05-19T14:30:00"
}
```

---

### 3.2 GET /incidentClassifier — Listar Incidentes

**Descrição:** Retorna todos os incidentes persistidos no banco H2, sem paginação.

| Campo | Valor |
|-------|-------|
| Método | `GET` |
| URL | `/incidentClassifier` |
| Autenticação | Não requerida |
| Paginação | **Não implementada** |

#### Request

```http
GET /incidentClassifier HTTP/1.1
Host: localhost:8080
Accept: application/json
```

#### Response — 200 OK

> ⚠️ Retorna **array direto** de `IncidentRecord[]`, sem wrapper e sem metadados de paginação.

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 2,
    "originalText": "Servidor web lento há 30 minutos",
    "formalizedIncidentText": "O servidor web apresenta latência elevada...",
    "category": "PERFORMANCE",
    "severity": "MEDIUM",
    "registeredAt": "2026-05-19T13:20:15",
    "suggestedAction": "Verificar carga do servidor e conexões ativas..."
  },
  {
    "id": 1,
    "originalText": "Sistema fora do ar desde as 08:00",
    "formalizedIncidentText": "O banco de dados principal está indisponível...",
    "category": "DATABASE",
    "severity": "HIGH",
    "registeredAt": "2026-05-19T14:35:22",
    "suggestedAction": "Verificar logs do DBMS..."
  }
]
```

#### Response — 200 OK (Lista vazia)

```json
[]
```

---

## 4. Endpoints NÃO Implementados

Os seguintes endpoints foram **especificados nos documentos anteriores mas não existem no backend**:

| Endpoint | Motivo |
|----------|--------|
| `GET /incidentClassifier/:id` | Não implementado no controller |
| `DELETE /incidentClassifier/:id` | Não implementado |
| `PUT /incidentClassifier/:id` | Não implementado |
| Qualquer endpoint com prefixo `/api/` | Base URL não tem prefixo `/api` |

---

## 5. HTTP Status Codes Retornados

| Código | Situação | Handler |
|--------|----------|---------|
| **200** | Classificação bem-sucedida / lista retornada | Controller |
| **400** | Campo `incident` vazio ou nulo (`@NotBlank`) | `GlobalExceptionHandler` |
| **422** | Falha ao processar resposta da IA | `GlobalExceptionHandler` |
| **500** | Erro genérico não tratado | `GlobalExceptionHandler` |

> **Não retornados pelo backend atual:** 201, 401, 403, 404, 408, 503

---

## 6. Validação

### 6.1 Validação do campo `incident`

| Regra | Anotação | Mensagem de Erro |
|-------|----------|-----------------|
| Obrigatório | `@NotBlank` | `"Incident description must not be blank"` |

> ⚠️ O backend **não valida tamanho mínimo ou máximo** do campo `incident`.  
> A validação de mínimo 10 / máximo 5000 caracteres **deve ser feita exclusivamente no frontend**.

### 6.2 Formato do erro de validação

Múltiplos campos inválidos são **concatenados** na string `message` com `;`:

```
"message": "incident: must not be blank; otherField: message"
```

---

## 7. Fluxo de Integração Frontend/Backend

```
┌─────────────────────────────────────────────┐
│  Frontend: Usuário preenche formulário       │
│  Validação local: mínimo 10, máximo 5000     │
│  Botão Classificar → dispatch NgRx action    │
└──────────────────────┬──────────────────────┘
                       │
         POST /incidentClassifier
         Content-Type: application/json
         { "incident": "texto..." }
                       │
┌──────────────────────▼──────────────────────┐
│  Backend: @Valid valida @NotBlank            │
│  → 400 se vazio                              │
│  IncidentClassifierService.incidentClassifier│
│  → Seleciona provider de IA                  │
│  → Chama API da IA com system prompt         │
│  → Parse resposta JSON                       │
│  → Persiste no H2 (IncidentRecord)           │
│  → Retorna IncidentClassification (200 OK)   │
└──────────────────────┬──────────────────────┘
                       │
         HTTP 200 OK
         { formalizedIncidentText, category, severity, registeredAt, suggestedAction }
                       │
┌──────────────────────▼──────────────────────┐
│  Frontend: Effect recebe resposta            │
│  → Dispatch classifyIncidentSuccess          │
│  → Reducer atualiza store                    │
│  → Component exibe resultado via async pipe  │
└─────────────────────────────────────────────┘
```

---

## 8. Exemplos Completos

### 8.1 Happy Path — POST com sucesso

**Request:**
```bash
curl -X POST http://localhost:8080/incidentClassifier \
  -H "Content-Type: application/json" \
  -d '{"incident": "O servidor de aplicação está respondendo com latência de 10 segundos. Logs mostram alto uso de memória (95%). CPU em 80%."}'
```

**Response (200 OK):**
```json
{
  "formalizedIncidentText": "O servidor de aplicação apresenta alta latência em requisições, indicando contenção de recursos. Métricas revelam saturação de memória (95%) e CPU (80%).",
  "category": "PERFORMANCE",
  "severity": "HIGH",
  "registeredAt": "2026-05-19T14:35:22",
  "suggestedAction": "1. Executar dump de threads; 2. Analisar logs de GC; 3. Verificar pool de conexões; 4. Reiniciar aplicação se necessário."
}
```

### 8.2 Erro — Campo vazio

**Request:**
```bash
curl -X POST http://localhost:8080/incidentClassifier \
  -H "Content-Type: application/json" \
  -d '{"incident": ""}'
```

**Response (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "incident: Incident description must not be blank",
  "timestamp": "2026-05-19T14:30:00"
}
```

### 8.3 Listar histórico

**Request:**
```bash
curl http://localhost:8080/incidentClassifier
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "originalText": "Sistema fora do ar desde as 08:00",
    "formalizedIncidentText": "O banco de dados principal está indisponível...",
    "category": "DATABASE",
    "severity": "HIGH",
    "registeredAt": "2026-05-19T14:35:22",
    "suggestedAction": "Verificar logs do DBMS..."
  }
]
```

---

## 9. Configuração do Backend

| Propriedade | Valor |
|-------------|-------|
| Porta | `8080` |
| Banco de dados | H2 (arquivo `./data/incident-classifier`) |
| Console H2 | `http://localhost:8080/h2-console` |
| Timestamps | ISO 8601 sem timezone (configurado via `write-dates-as-timestamps=false`) |
| CORS | Não configurado explicitamente — verificar se necessário para o frontend |

---

**Versão:** 2.0
**Última Atualização:** Maio 2026 — Revisado e validado contra código-fonte do backend
