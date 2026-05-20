# SICO Frontend

Interface web do **Sistema de Classificação de Ocorrências (SICO)**, construída com Angular 17, Angular Material e NgRx.

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|------------|---------------|
| Node.js    | 18.x          |
| npm        | 9.x           |
| Angular CLI | 17.x         |

Instale o Angular CLI globalmente (caso ainda não tenha):

```bash
npm install -g @angular/cli@17
```

---

## Configuração

### 1. Instalar dependências

```bash
cd frontend
npm install
```

### 2. Variáveis de ambiente

Os arquivos de ambiente ficam em [src/environments/](src/environments/):

| Arquivo | Uso |
|---------|-----|
| [environment.ts](src/environments/environment.ts) | Desenvolvimento local |
| [environment.prod.ts](src/environments/environment.prod.ts) | Build de produção |

Configuração padrão de desenvolvimento:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};
```

Ajuste `apiUrl` caso o backend rode em uma porta diferente.

### 3. Proxy para o backend

O arquivo [proxy.conf.json](proxy.conf.json) redireciona chamadas ao backend durante o desenvolvimento:

```json
{
  "/incidentClassifier": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

Todas as requisições feitas para `/incidentClassifier/*` são automaticamente encaminhadas ao backend. Não é necessário alterar esse arquivo na maioria dos casos.

---

## Inicialização

### Desenvolvimento

```bash
npm start
# ou
ng serve
```

O servidor de desenvolvimento inicia em `http://localhost:4200` com hot-reload e proxy configurado automaticamente.

### Build de produção

```bash
ng build
```

Os artefatos compilados são gerados em `dist/sico-frontend/`.

### Testes

```bash
# Executar testes uma vez
npm test

# Modo watch (re-executa ao salvar)
npm run test:watch

# Com relatório de cobertura
npm run test:coverage
```

---

## Estrutura de pastas

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                     # Infraestrutura da aplicação
│   │   │   ├── interceptors/         # Interceptors HTTP (erro e loading)
│   │   │   ├── models/               # Interfaces e enums de domínio
│   │   │   ├── services/             # Serviços HTTP e notificações
│   │   │   └── store/                # Estado global NgRx
│   │   │       ├── incident/         # Actions, reducer, effects e selectors
│   │   │       └── ui/               # Estado da interface (loading, erros)
│   │   ├── modules/
│   │   │   └── incident/             # Módulo de ocorrências (feature module)
│   │   │       ├── components/       # Componentes reutilizáveis do módulo
│   │   │       │   ├── incident-category-badge/
│   │   │       │   └── incident-severity-badge/
│   │   │       ├── pages/            # Páginas navegáveis
│   │   │       │   ├── incident-form/     # Formulário de registro
│   │   │       │   └── incident-history/  # Histórico de ocorrências
│   │   │       └── incident.routes.ts
│   │   ├── shared/                   # Componentes e utilitários globais
│   │   │   ├── components/
│   │   │   │   ├── error-alert/
│   │   │   │   ├── header/
│   │   │   │   └── loading-spinner/
│   │   │   └── pipes/
│   │   │       └── format-date.pipe.ts
│   │   ├── app.component.ts
│   │   ├── app.config.ts             # Bootstrap standalone da aplicação
│   │   └── app.routes.ts             # Rotas raiz
│   ├── environments/
│   ├── styles/                       # Estilos globais e tema Material
│   ├── index.html
│   └── main.ts
├── angular.json
├── proxy.conf.json
├── tsconfig.json
└── package.json
```

### Path aliases (tsconfig)

Use os aliases para importações absolutas, evitando caminhos relativos longos:

| Alias | Aponta para |
|-------|-------------|
| `@core/*` | `src/app/core/*` |
| `@shared/*` | `src/app/shared/*` |
| `@modules/*` | `src/app/modules/*` |
| `@environments/*` | `src/environments/*` |

---

## Rotas

| URL | Componente | Título da página |
|-----|-----------|-----------------|
| `/` | — | Redireciona para `/incident/new` |
| `/incident/new` | `IncidentFormComponent` | Registrar Incidente — SICO |
| `/incident/history` | `IncidentHistoryComponent` | Histórico — SICO |
| `/**` | — | Redireciona para `/incident/new` |

O módulo `incident` é carregado via **lazy loading**; o bundle principal não inclui o código das páginas de ocorrência até a primeira navegação.

---

## Estado global (NgRx)

A aplicação usa NgRx para gerenciar estado. Os slices disponíveis são:

### `incident`

| Propriedade | Tipo | Descrição |
|-------------|------|-----------|
| `incidents` | `IncidentRecord[]` | Histórico de ocorrências |
| `classification` | `IncidentClassification \| null` | Resultado da última classificação |
| `loading` | `boolean` | Requisição em andamento |
| `error` | `string \| null` | Mensagem de erro atual |

### `ui`

Controla estados visuais globais (spinner de carregamento, visibilidade de alertas).

---

## Modelos principais

### `IncidentCategory`

Categorias disponíveis para classificação: `SECURITY`, `NETWORK`, `HARDWARE`, `SOFTWARE`, `DATABASE`, `ACCESS`, `PERFORMANCE`, `BACKUP`, `COMPLIANCE`, `INFRASTRUCTURE`, `MONITORING`, `SUPPORT`, `OTHER`.

### `IncidentSeverity`

`LOW` | `MEDIUM` | `HIGH`

### `IncidentRequest`

```typescript
{ description: string }  // mín. 10, máx. 5000 caracteres
```

---

## Principais dependências

| Pacote | Versão | Finalidade |
|--------|--------|-----------|
| `@angular/core` | 17.3 | Framework |
| `@angular/material` | 17.3 | Componentes UI |
| `@ngrx/store` | 17.2 | Gerenciamento de estado |
| `@ngrx/effects` | 17.2 | Efeitos colaterais assíncronos |
| `rxjs` | 7.8 | Programação reativa |
| `jest` | 29.7 | Framework de testes |
