# Sistema Inteligente de Classificação de Ocorrências — Diagrama UML (SICO)

---

## 1. Arquitetura Resumida

### 1.1. Backend

```
Controller → Service → ApiProvider (interface)
                ↓              ↓
          Repository    [OpenAI | Gemini | Anthropic]
                ↓
           H2 Database
```

- **Controller**: recebe e valida a requisição REST
- **Service**: orquestra a chamada à IA e a persistência
- **ApiProvider**: interface que abstrai o provider de LLM
- **Repository**: acesso ao banco via Spring Data JPA

### 1.2. Frontend

```
Pages → NgRx Store → Effects → Services → Backend API
                ↓
          Components (UI)
```

- **Pages**: telas de registro e histórico
- **NgRx Store**: estado global da aplicação
- **Services**: chamadas HTTP ao backend
- **Components**: badges, spinner, alertas

### 1.3. Fluxograma do sistema

```mermaid
flowchart TB

%% =========================
%% FRONTEND
%% =========================
subgraph FE["Frontend - Angular"]
direction TB
A[Operador registra ocorrência]
B[Formulário envia requisição REST]
C[Exibição do resultado processado]
end

%% =========================
%% BACKEND
%% =========================
subgraph BE["Backend - Spring Boot"]
direction TB
D[Controller recebe requisição]
E[Service valida dados de entrada]
F[Service monta prompt para IA]
G[Processa resposta estruturada]
H[Valida categoria e gravidade]
I[Salva ocorrência no banco]
end

%% =========================
%% IA / LLM
%% =========================
subgraph AI["IA / LLM"]
direction TB
J[Envio do prompt]
K[LLM gera resposta estruturada]
end

%% =========================
%% DATABASE
%% =========================
subgraph DB["Banco de Dados - H2"]
direction TB
L[(Tabela de Ocorrências)]
end

%% =========================
%% FLOW
%% =========================
A --> B
B --> D
D --> E
E --> F
F --> J
J --> K
K --> G
G --> H
H --> I
I --> L
I --> C
```

---

## 2. Diagramas

### 2.1. Frontend

#### 2.1.1. Visão em camadas da estrutura

```mermaid
graph LR
    subgraph App[Aplicação Angular]
        ROUTES[Rotas e Páginas]
        APP_ROOT[App Root / Configuração padrão]
    end

    subgraph Pages[Páginas]
        FORM[IncidentFormComponent]
        HISTORY[IncidentHistoryComponent]
    end

    subgraph Store[NgRx Store]
        ACTIONS[incident.actions.ts]
        EFFECTS[IncidentEffects]
        REDUCER[incident.reducer.ts]
        SELECTORS[incident.selectors.ts]
    end

    subgraph Services[Serviços]
        INCIDENT_SERVICE[IncidentService]
        HTTP_CLIENT[HttpClientService]
        NOTIFICATION[NotificationService]
    end

    subgraph UI[Componentes Compartilhados]
        HEADER[HeaderComponent]
        ALERT[ErrorAlertComponent]
        SPINNER[LoadingSpinnerComponent]
    end

    subgraph Interceptors[Interceptors]
        ERROR_INTERCEPTOR[error.interceptor.ts]
        LOADING_INTERCEPTOR[loading.interceptor.ts]
    end

    subgraph Backend[API Backend]
        API[/POST & GET /incidentClassifier/]
    end

    APP_ROOT --> ROUTES
    ROUTES --> FORM
    ROUTES --> HISTORY
    FORM --> ACTIONS
    HISTORY --> ACTIONS
    ACTIONS --> EFFECTS
    EFFECTS --> INCIDENT_SERVICE
    INCIDENT_SERVICE --> HTTP_CLIENT
    HTTP_CLIENT --> API

    FORM --> HEADER
    FORM --> ALERT
    FORM --> SPINNER
    HISTORY --> HEADER
    HISTORY --> ALERT

    ERROR_INTERCEPTOR --> HTTP_CLIENT
    LOADING_INTERCEPTOR --> HTTP_CLIENT
    SELECTORS --> FORM
    SELECTORS --> HISTORY
    REDUCER --> SELECTORS
```

---

### 2.2. Backend

#### 2.2.1. Visão em camadas da estrutura

```mermaid
graph LR
    subgraph Presentation ["Camada de Apresentação"]
        CTR["IncidentClassifierController\n@RestController"]
    end

    subgraph Business ["Camada de Negócio"]
        SVC["IncidentClassifierService\n@Service"]
        SPC["SystemPromptConfig\n@Configuration"]
    end

    subgraph Provider ["Camada de Provider de IA"]
        direction TB
        API["ApiProvider\n&laquo;interface&raquo;"]
        ANT["AnthropicApiProvider"]
        OAI["OpenAiApiProvider"]
        GEM["GeminiApiProvider"]
        API --> ANT
        API --> OAI
        API --> GEM
    end

    subgraph Data ["Camada de Dados"]
        REP["IncidentRepository\n&laquo;JpaRepository&raquo;"]
        H2[("H2 Database\nincident_records")]
        REP --> H2
    end

    subgraph External ["Externo"]
        LLM["LLM API\nAnthropic / OpenAI / Gemini"]
    end

    CTR --> SVC
    SVC --> API
    SVC --> REP
    SPC -. "systemPrompt @Bean" .-> SVC
    ANT --> LLM
    OAI --> LLM
    GEM --> LLM
```

---

#### 2.2.2. Diagrama de classes

Estrutura das classes, atributos, métodos e relacionamentos.

```mermaid
classDiagram
    class IncidentClassifierController {
        -IncidentClassifierService service
        +classifyIncident(IncidentRequest) ResponseEntity
        +listIncidents() ResponseEntity
    }

    class IncidentClassifierService {
        -ApiProvider apiProvider
        -String systemPrompt
        -ObjectMapper objectMapper
        -IncidentRepository incidentRepository
        +incidentClassifier(String) IncidentClassification
        +listIncidents() List~IncidentRecord~
        -cleanJsonResponse(String) String
    }

    class ApiProvider {
        <<interface>>
        +getResponse(String, String, List~Tool~) String
    }

    class AnthropicApiProvider {
        -String apiKey
        -String model
        -String apiUrl
        -String version
        -int maxTokens
        +getResponse(String, String, List~Tool~) String
    }

    class OpenAiApiProvider {
        -String apiKey
        -String model
        -int maxTokens
        +getResponse(String, String, List~Tool~) String
    }

    class GeminiApiProvider {
        -String apiKey
        -String model
        -int maxOutputTokens
        -Client client
        +getResponse(String, String, List~Tool~) String
    }

    class IncidentRepository {
        <<interface>>
    }

    class IncidentRecord {
        +Long id
        +String originalText
        +String formalizedIncidentText
        +IncidentCategory category
        +IncidentSeverity severity
        +LocalDateTime registeredAt
        +String suggestedAction
    }

    class IncidentClassification {
        +String formalizedIncidentText
        +IncidentCategory category
        +IncidentSeverity severity
        +LocalDateTime registeredAt
        +String suggestedAction
    }

    class IncidentRequest {
        +String incident
    }

    class ErrorResponse {
        <<record>>
        +int status
        +String error
        +String message
        +LocalDateTime timestamp
        +of(int, String, String) ErrorResponse
    }

    class GlobalExceptionHandler {
        <<RestControllerAdvice>>
        +handleIncidentClassificationException(ex) ResponseEntity
        +handleValidationException(ex) ResponseEntity
        +handleUnexpectedException(ex) ResponseEntity
    }

    class IncidentCategory {
        <<enumeration>>
        SECURITY
        NETWORK
        HARDWARE
        SOFTWARE
        DATABASE
        DATA_LOSS
        ACCESS_CONTROL
        PERFORMANCE
        SERVICE_OUTAGE
        INFRASTRUCTURE
        COMMUNICATION
        COMPLIANCE
        OTHER
    }

    class IncidentSeverity {
        <<enumeration>>
        LOW
        MEDIUM
        HIGH
    }

    IncidentClassifierController --> IncidentClassifierService
    IncidentClassifierController ..> IncidentRequest : usa
    IncidentClassifierController ..> IncidentClassification : retorna
    IncidentClassifierController ..> IncidentRecord : retorna
    IncidentClassifierController --> GlobalExceptionHandler

    IncidentClassifierService --> ApiProvider
    IncidentClassifierService --> IncidentRepository
    IncidentClassifierService ..> IncidentClassification : cria
    IncidentClassifierService ..> IncidentRecord : persiste

    ApiProvider <|.. AnthropicApiProvider
    ApiProvider <|.. OpenAiApiProvider
    ApiProvider <|.. GeminiApiProvider

    IncidentRepository --> IncidentRecord : gerencia

    IncidentRecord --> IncidentCategory
    IncidentRecord --> IncidentSeverity
    IncidentClassification --> IncidentCategory
    IncidentClassification --> IncidentSeverity
```

---

#### 2.2.3. Diagrama de Sequência — Fluxo Principal (POST)

Fluxo completo de registro e classificação de uma ocorrência.

```mermaid
sequenceDiagram
    actor Usuário as Usuário
    participant Controller as IncidentClassifierController
    participant Service as IncidentClassifierService
    participant Provider as ApiProvider
    participant Repository as IncidentRepository
    participant Database as H2 Database
    participant LLM as LLM API

    Usuário->>Controller: POST /incidentClassifier { incident }
    Controller->>Service: classifyIncident(request.incident)
    Service->>Provider: getResponse(systemPrompt, userPrompt, tools)
    Provider->>LLM: request classification
    LLM-->>Provider: structured response
    Provider-->>Service: JSON string
    Service->>Repository: save(IncidentRecord)
    Repository->>Database: persist record
    Service-->>Controller: IncidentClassification
    Controller-->>Frontend: 200 OK + classification
```

---

#### 2.2.4. Diagrama de Sequência — Listagem do Histórico (GET)

Fluxo de consulta ao histórico de ocorrências.

```mermaid
sequenceDiagram
    actor Usuário as Usuário
    participant Controller as IncidentClassifierController
    participant Service as IncidentClassifierService
    participant Repository as IncidentRepository
    participant Database as H2 Database

    Usuário->>Controller: GET /incidentClassifier
    Controller->>Service: listIncidents()
    Service->>Repository: findAll()
    Repository->>Database: SELECT * FROM incident_records
    Database-->>Repository: rows
    Repository-->>Service: List<IncidentRecord>
    Service-->>Controller: List<IncidentRecord>
    Controller-->>Usuário: 200 OK + incident list
```
