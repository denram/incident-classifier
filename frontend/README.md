# Sistema Inteligente de Classificação de Ocorrências — Frontend (SICO)

Interface web em Angular para o sistema **SICO** (Sistema Inteligente de Classificação de Ocorrências), que permite a operadores de portaria registrar ocorrências em linguagem natural e visualizar o resultado estruturado gerado pela IA.

## Sumário

- [1. Objetivo da Interface](#1-objetivo-da-interface)
- [2. IA como Ferramenta de Desenvolvimento](#2-ia-como-ferramenta-de-desenvolvimento)
- [3. Arquitetura](#3-arquitetura)
  - [3.1. Tecnologias](#31-tecnologias)
  - [3.2. Estrutura de Pastas](#32-estrutura-de-pastas)
  - [3.3. Rotas](#33-rotas)
  - [3.4. Fluxo da Aplicação](#34-fluxo-da-aplicação)
    - [3.4.1. Registro de ocorrência](#341-registro-de-ocorrência)
    - [3.4.2. Histórico](#342-histórico)
  - [3.5. Componentes Principais](#35-componentes-principais)
    - [3.5.1. `IncidentFormComponent` — `/incident/new`](#351-incidentformcomponent--incidentnew)
    - [3.5.2. `IncidentHistoryComponent` — `/incident/history`](#352-incidenthistorycomponent--incidenthistory)
    - [3.5.3. `IncidentCategoryBadgeComponent`](#353-incidentcategorybadgecomponent)
    - [3.5.4. `IncidentSeverityBadgeComponent`](#354-incidentseveritybadgecomponent)
    - [3.5.5. `LoadingSpinnerComponent`](#355-loadingspinnercomponent)
    - [3.5.6. `ErrorAlertComponent`](#356-erroralertcomponent)
  - [3.6. Integração com o Backend](#36-integração-com-o-backend)
    - [3.6.1. Proxy (desenvolvimento local)](#361-proxy-desenvolvimento-local)
- [4. Como executar localmente](#4-como-executar-localmente)
  - [4.1. Pré-requisitos](#41-pré-requisitos)
  - [4.2. Passos](#42-passos)
  - [4.3. Configuração da API](#43-configuração-da-api)
  - [4.4. Testes](#44-testes)
- [5. Interface Visual Esperada](#5-interface-visual-esperada)
  - [5.1. Tela de Registro](#51-tela-de-registro)
  - [5.2. Tela de Histórico](#52-tela-de-histórico)
- [6. Limitações Conhecidas](#6-limitações-conhecidas)
- [7. Melhorias Futuras](#7-melhorias-futuras)

---

## 1. Objetivo da Interface

Oferecer uma tela simples e objetiva onde o operador:

1. **Descreve** a ocorrência com suas próprias palavras
2. **Envia** para processamento pelo backend com IA
3. **Visualiza** o retorno estruturado: texto formalizado, categoria, gravidade e ação sugerida
4. **Consulta** o histórico de ocorrências registradas

---

## 2. IA como Ferramenta de Desenvolvimento

A IA foi usada como assistente de codificação (vibe coding) durante o desenvolvimento.
O frontend apenas exibe os dados processados pelo backend:

| Campo                    | Descrição                                     |
| ------------------------ | --------------------------------------------- |
| `formalizedIncidentText` | Texto reescrito de forma profissional         |
| `category`               | Categoria classificada (ex: `SERVICE_OUTAGE`) |
| `severity`               | Gravidade definida (`LOW`, `MEDIUM`, `HIGH`)  |
| `suggestedAction`        | Ação recomendada para o operador              |

---

## 3. Arquitetura

A aplicação segue uma arquitetura Angular baseada em componentes standalone com gerenciamento de estado centralizado via NgRx:

```
Pages (UI)
  └── NgRx Store (estado global)
        ├── Actions  → disparam intenções
        ├── Effects  → chamam os Services (HTTP)
        ├── Reducer  → atualiza o estado
        └── Selectors → fornecem dados aos componentes

Services
  └── HttpClientService → encapsula chamadas HTTP com timeout
        └── IncidentService → endpoints do backend
```

- **Componentes standalone**: sem módulos, estrutura mais simples
- **NgRx**: estado previsível, sem acoplamento entre componentes e serviços
- **Angular Material**: UI consistente sem estilização manual extensa
- **Proxy**: requisições ao backend passam pelo servidor de desenvolvimento local

---

### 3.1. Tecnologias

| Tecnologia       | Versão | Uso                     |
| ---------------- | ------ | ----------------------- |
| Angular          | 17     | Framework principal     |
| TypeScript       | —      | Linguagem               |
| NgRx             | —      | Gerenciamento de estado |
| Angular Material | 17     | Componentes de UI       |
| RxJS             | —      | Programação reativa     |
| Jest             | —      | Testes unitários        |
| SCSS             | —      | Estilização             |

---

### 3.2. Estrutura de Pastas

```
frontend/src/
├── app/
│   ├── app.component.ts          # Componente raiz
│   ├── app.config.ts             # Configuração da aplicação (providers, store)
│   ├── app.routes.ts             # Rotas principais
│   │
│   ├── core/
│   │   ├── interceptors/
│   │   │   ├── error.interceptor.ts      # Captura erros HTTP globalmente
│   │   │   └── loading.interceptor.ts    # Controla estado de carregamento
│   │   ├── models/
│   │   │   └── incident.model.ts         # Interfaces e enums do domínio
│   │   ├── services/
│   │   │   ├── http-client.service.ts    # Wrapper HTTP com timeout
│   │   │   ├── incident.service.ts       # Chamadas à API do backend
│   │   │   └── notification.service.ts  # Notificações da UI
│   │   └── store/
│   │       └── incident/
│   │           ├── incident.actions.ts   # Ações NgRx
│   │           ├── incident.effects.ts   # Side effects (chamadas HTTP)
│   │           ├── incident.reducer.ts   # Atualização de estado
│   │           ├── incident.selectors.ts # Seletores de estado
│   │           └── incident.state.ts     # Definição do estado
│   │
│   ├── modules/
│   │   └── incident/
│   │       ├── components/
│   │       │   ├── incident-category-badge/  # Badge colorido de categoria
│   │       │   └── incident-severity-badge/  # Badge colorido de gravidade
│   │       ├── pages/
│   │       │   ├── incident-form/            # Página de registro
│   │       │   └── incident-history/         # Página de histórico
│   │       └── incident.routes.ts            # Rotas do módulo
│   │
│   └── shared/
│       ├── components/
│       │   ├── error-alert/          # Componente de exibição de erro
│       │   ├── header/               # Cabeçalho da aplicação
│       │   └── loading-spinner/      # Indicador de carregamento
│       └── pipes/
│           └── format-date.pipe.ts   # Formatação de datas
│
├── environments/
│   ├── environment.ts              # Dev: apiUrl = http://localhost:8080
│   └── environment.prod.ts         # Prod: configurar URL do backend
├── styles/
│   ├── global.scss
│   ├── theme.scss
│   ├── _variables.scss
│   └── _mixins.scss
└── index.html
```

---

### 3.3. Rotas

| Rota                | Componente                 | Descrição                            |
| ------------------- | -------------------------- | ------------------------------------ |
| `/incident/new`     | `IncidentFormComponent`    | Formulário de registro de ocorrência |
| `/incident/history` | `IncidentHistoryComponent` | Tabela com histórico de ocorrências  |
| `/`                 | —                          | Redireciona para `/incident/new`     |

---

### 3.4. Fluxo da Aplicação

#### 3.4.1. Registro de ocorrência

```
1. Operador acessa /incident/new
2. Preenche o campo de texto (mínimo 10, máximo 5000 caracteres)
3. Clica em "Registrar"
4. Store despacha a action → Effect chama POST /incidentClassifier
5. Loading spinner é exibido durante o processamento
6. Resultado da IA é exibido: texto formalizado, categoria, gravidade e ação sugerida
7. Em caso de erro, componente de alerta é exibido
```

#### 3.4.2. Histórico

```
1. Operador acessa /incident/history
2. Store despacha a action → Effect chama GET /incidentClassifier
3. Tabela é populada com as ocorrências persistidas
4. Colunas: ID, Data, Categoria, Gravidade, Texto Original, Ação Sugerida
5. A tabela permite ordenação por coluna
```

---

### 3.5. Componentes Principais

#### 3.5.1. `IncidentFormComponent` — `/incident/new`

- Formulário reativo com validação de tamanho mínimo/máximo
- Exibe contador de caracteres em tempo real
- Após retorno bem-sucedido, exibe card com resultado estruturado da IA
- Badges coloridos para categoria e gravidade

#### 3.5.2. `IncidentHistoryComponent` — `/incident/history`

- Tabela Angular Material com ordenação
- Colunas: ID, data, categoria, gravidade, texto original, ação sugerida
- Botão de atualização para recarregar os dados

#### 3.5.3. `IncidentCategoryBadgeComponent`

- Exibe a categoria do incidente como badge com rótulo em português

#### 3.5.4. `IncidentSeverityBadgeComponent`

- Exibe a gravidade com cores distintas: verde (LOW), amarelo (MEDIUM), vermelho (HIGH)

#### 3.5.5. `LoadingSpinnerComponent`

- Exibido automaticamente durante requisições via interceptor de loading

#### 3.5.6. `ErrorAlertComponent`

- Exibe mensagem de erro retornada pelo backend

---

### 3.6. Integração com o Backend

O frontend se comunica com o backend via `HttpClientService`, que encapsula o `HttpClient` do Angular com:

- **Timeout de 30s** para requisições POST (aguarda o processamento da LLM)
- **Timeout de 5s** para requisições GET
- Tratamento centralizado de erros via interceptor

#### 3.6.1. Proxy (desenvolvimento local)

O arquivo `proxy.conf.json` redireciona chamadas de `/incidentClassifier` para o backend em `http://localhost:8080`, evitando problemas de CORS durante o desenvolvimento:

```json
{
  "/incidentClassifier": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

---

## 4. Como executar localmente

### 4.1. Pré-requisitos

- Node.js 18+
- npm
- Backend rodando em `http://localhost:8080`

### 4.2. Passos

```bash
# 1. Acesse a pasta do frontend
cd incident-classifier/frontend

# 2. Instale as dependências
npm install

# 3. Inicie o servidor de desenvolvimento
npm start
```

A aplicação estará disponível em **http://localhost:4200**.

---

### 4.3. Configuração da API

O endereço do backend é definido em `src/environments/environment.ts` tendo a seguinte configuração padrão de desenvolvimento

```typescript
export const environment = {
  production: false,
  apiUrl: "http://localhost:8080",
};
```

---

### 4.4. Testes

```bash
# Executar testes
npm test

# Executar com watch mode
npm run test:watch

# Gerar relatório de cobertura
npm run test:coverage
```

Os testes utilizam **Jest**. Existe teste unitário para o pipe `format-date` e para o reducer do NgRx.

---

## 5. Interface Visual Esperada

### 5.1. Tela de Registro

`/incident/new`

```
┌─────────────────────────────────────────────┐
│  SICO — Registro de Ocorrência              │
├─────────────────────────────────────────────┤
│  Descreva a ocorrência:                     │
│  ┌─────────────────────────────────────┐    │
│  │ cara tentou entrar sem cadastro...  │    │
│  └─────────────────────────────────────┘    │
│  42/5000 caracteres                         │
│                                             │
│  [ Registrar Ocorrência ]                   │
├─────────────────────────────────────────────┤
│  Resultado da IA:                           │
│  Texto: Visitante sem cadastro tentou...    │
│  Categoria: [ Segurança ]                   │
│  Gravidade: [ HIGH ]                        │
│  Ação: Acionar equipe de segurança...       │
└─────────────────────────────────────────────┘
```

### 5.2. Tela de Histórico

`/incident/history`

```
┌────┬────────────┬─────────────┬──────────┬───────────────────┐
│ ID │ Data       │ Categoria   │ Grav.    │ Texto Original    │
├────┼────────────┼─────────────┼──────────┼───────────────────┤
│  1 │ 19/05/2026 │ [Segurança] │ [HIGH]   │ cara tentou...    │
│  2 │ 19/05/2026 │ [Software]  │ [MEDIUM] │ sistema caiu...   │
└────┴────────────┴─────────────┴──────────┴───────────────────┘
```

---

## 6. Limitações Conhecidas

- Não há autenticação de usuário (fora do escopo do MVP)
- Não há paginação na tabela de histórico
- Não há forma de editar ou excluir ocorrência

---

## 7. Melhorias Futuras

- Adicionar filtros e paginação na tabela de histórico
- Implementar autenticação com login de operador
- Adicionar página de detalhes de uma ocorrência específica
- Notificações em tempo real de novas ocorrências
