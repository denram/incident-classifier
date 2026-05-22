# Prompt: Geração do Frontend Angular 17 — SICO

**Sistema:** Sistema Inteligente de Classificação de Ocorrências (SICO)
**Stack:** Angular 17 · Angular Material · NgRx · RxJS · TypeScript strict
**Versão:** 2.0 — validado contra o código-fonte do backend Spring Boot

---

## Contexto

Você é um engenheiro Angular sênior. Gere o frontend completo do SICO seguindo **estritamente** os padrões abaixo. Não invente abstrações além do necessário. Não adicione funcionalidades fora do escopo.

---

## 1. Arquitetura Obrigatória

### 1.1 Estrutura de Pastas

```
frontend/src/app/
├── core/
│   ├── interceptors/
│   │   ├── error.interceptor.ts
│   │   └── loading.interceptor.ts
│   ├── services/
│   │   ├── incident.service.ts      ← nome alinhado ao backend
│   │   ├── http-client.service.ts
│   │   └── notification.service.ts
│   ├── models/
│   │   └── incident.model.ts        ← nome alinhado ao backend
│   └── store/
│       ├── incident/
│       │   ├── incident.state.ts
│       │   ├── incident.actions.ts
│       │   ├── incident.reducer.ts
│       │   ├── incident.selectors.ts
│       │   └── incident.effects.ts
│       └── ui/
│           ├── ui.state.ts
│           ├── ui.actions.ts
│           └── ui.reducer.ts
├── shared/
│   ├── components/
│   │   ├── header/
│   │   ├── loading-spinner/
│   │   └── error-alert/
│   ├── pipes/
│   │   └── format-date.pipe.ts
│   └── utils/
│       └── validators.util.ts
├── modules/
│   └── incident/
│       ├── incident.routes.ts
│       ├── pages/
│       │   ├── occurrence-form/
│       │   ├── occurrence-result/
│       │   ├── occurrence-history/
│       │   └── occurrence-detail/
│       └── components/
│           ├── incident-category-badge/
│           └── incident-severity-badge/
├── app.routes.ts
├── app.config.ts
└── app.component.ts
```

### 1.2 Regras de Organização

- **Todos os components são standalone** (`standalone: true`)
- **`ChangeDetectionStrategy.OnPush`** em todos os components apresentacionais e smart
- **Imports explícitos** no decorator `@Component` — sem `SharedModule` genérico
- **TypeScript strict mode** ativado; proibido `any`
- **Path aliases** no `tsconfig.json`:
  ```json
  "@core/*": ["src/app/core/*"],
  "@shared/*": ["src/app/shared/*"],
  "@modules/*": ["src/app/modules/*"],
  "@environments/*": ["src/environments/*"]
  ```

---

## 2. Modelos Tipados (Models & DTOs)

Gere os modelos abaixo **exatamente** com esses tipos. Foram validados contra o código-fonte do backend (`IncidentRequest`, `IncidentClassification`, `IncidentRecord`, `IncidentCategory`, `IncidentSeverity`).

### 2.1 `core/models/incident.model.ts`

```typescript
// Espelha IncidentCategory.java do backend (13 valores)
export enum IncidentCategory {
  SECURITY        = 'SECURITY',
  NETWORK         = 'NETWORK',
  HARDWARE        = 'HARDWARE',
  SOFTWARE        = 'SOFTWARE',
  DATABASE        = 'DATABASE',
  DATA_LOSS       = 'DATA_LOSS',
  ACCESS_CONTROL  = 'ACCESS_CONTROL',
  PERFORMANCE     = 'PERFORMANCE',
  SERVICE_OUTAGE  = 'SERVICE_OUTAGE',
  INFRASTRUCTURE  = 'INFRASTRUCTURE',
  COMMUNICATION   = 'COMMUNICATION',
  COMPLIANCE      = 'COMPLIANCE',
  OTHER           = 'OTHER'
}

// Espelha IncidentSeverity.java do backend (3 valores — sem CRITICAL/CRITICA)
export enum IncidentSeverity {
  LOW    = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH   = 'HIGH'
}

// Espelha IncidentRequest.java — campo 'incident', sem validação de tamanho no backend
export interface IncidentRequest {
  incident: string; // @NotBlank — validação mín. 10 / máx. 5000 chars APENAS no frontend
}

// Espelha IncidentClassification.java — retornado diretamente no POST (sem wrapper)
export interface IncidentClassification {
  formalizedIncidentText: string;  // campo: formalizedIncidentText (não formalizedOccurrenceText)
  category: IncidentCategory;
  severity: IncidentSeverity;
  registeredAt: string;            // ISO 8601 sem timezone ex.: "2026-05-19T14:35:22"
  suggestedAction: string;
}

// Espelha IncidentRecord.java — retornado no GET lista (array direto, sem wrapper)
export interface IncidentRecord extends IncidentClassification {
  id: number;          // Long auto-incrementado (1, 2, 3...) — NÃO é UUID
  originalText: string;
}

// Espelha ErrorResponse.java (record) — campos: status, error, message, timestamp
export interface ApiError {
  status: number;      // campo: 'status' (não 'statusCode')
  error: string;
  message: string;
  timestamp: string;
}

// Estado local do formulário (não vem da API)
export interface IncidentFormState {
  originalText: string;
  classification: IncidentClassification | null;
  error: string | null;
  isLoading: boolean;
  isSubmitted: boolean;
}
```

---

## 3. Contrato de API (validado contra backend)

> **Base URL:** `environment.apiUrl` → `http://localhost:8080` (sem prefixo `/api`)

| Método | Endpoint               | Body             | Response                  | Status |
|--------|------------------------|------------------|---------------------------|--------|
| POST   | `/incidentClassifier`  | `IncidentRequest`| `IncidentClassification`  | 200    |
| GET    | `/incidentClassifier`  | —                | `IncidentRecord[]`        | 200    |

> ⚠️ **GET por ID não existe no backend.** Não gerar service method, action ou rota para isso.  
> ⚠️ **Sem paginação.** O GET retorna array completo sem `skip`/`take`.  
> ⚠️ **Sem autenticação implementada.** Não gerar `authInterceptor` nem `authGuard`.  
> ⚠️ **Sem wrapper.** A API retorna o objeto/array diretamente, sem envelope `{ success, data }`.

### 3.1 Tratamento de Erros HTTP (mapeamento real do GlobalExceptionHandler)

| Status | Handler no backend | Mensagem ao usuário |
|--------|-------------------|---------------------|
| 400    | `@NotBlank` falhou | Exibir `error.message` da `ApiError` |
| 422    | IA falhou ao classificar | "Falha ao classificar o incidente. Tente novamente." |
| 500    | Erro genérico | "Erro no servidor. Tente novamente." |
| 0      | Sem conexão | "Erro de conexão. Verifique sua internet." |

> **Não retornados pelo backend:** 201, 401, 403, 404, 408, 503. Não gerar handlers para esses códigos.

---

## 4. Regras de Negócio para a UI

### 4.1 Formulário de Ocorrência (`/occurrence`)

- Campo `occurrence`: textarea obrigatório, mínimo 10 e máximo 5.000 caracteres
- Validação **em tempo real** com `mat-error` (required, minlength, maxlength)
- Contador de caracteres visível: `"X / 5000"`
- Botão **"Classificar"** desabilitado se formulário inválido ou loading ativo
- Spinner de carregamento durante POST (pode demorar até 30 s — IA)
- Após sucesso: exibir resultado inline (não redirecionar)
- Após erro: exibir `mat-error` com mensagem do backend

### 4.2 Card de Resultado

Exibir após classificação bem-sucedida:
- **Texto Formalizado** — tipografia `body-1`, card com borda
- **Categoria** — badge colorido (ver cores abaixo)
- **Severidade** — badge colorido (ver cores abaixo)
- **Ação Sugerida** — destacada, fonte diferente
- **Data/hora** — formatada em pt-BR (`dd/MM/yyyy HH:mm`)
- Botão "Nova Ocorrência" para limpar e reclassificar

### 4.3 Histórico (`/occurrence/history`)

- Tabela Material (`mat-table`) com colunas: Data, Categoria, Severidade, Texto (primeiros 80 chars), Ações
- Paginação: `mat-paginator` com `pageSize=10`, opcões `[10, 20, 50]`
- Ordenação padrão: `registeredAt` DESC
- Clique em linha → navegar para `/occurrence/:id`
- Loading state: skeleton ou spinner enquanto carrega
- Estado vazio: mensagem "Nenhuma ocorrência registrada."

### 4.4 Detalhe (`/occurrence/:id`)

- Exibir todos os campos de `OccurrenceRecord`
- `originalText` e `formalizedOccurrenceText` lado a lado (grid responsivo)
- Botão "Voltar" (`router.back()`)
- Tratamento de 404: redirecionar para histórico com `notification.error(...)`

### 4.5 Cores e Badges

**Severidade:**
```
LOW    → verde   #28a745  (mat-chip color="accent")
MEDIUM → amarelo #ffc107  (mat-chip custom class)
HIGH   → vermelho #dc3545 (mat-chip color="warn")
```

**Categoria:** badge neutro (cor primária) com label legível em português:
```
SECURITY        → Segurança
NETWORK         → Rede
HARDWARE        → Hardware
SOFTWARE        → Software
DATABASE        → Banco de Dados
DATA_LOSS       → Perda de Dados
ACCESS_CONTROL  → Controle de Acesso
PERFORMANCE     → Performance
SERVICE_OUTAGE  → Serviço Indisponível
INFRASTRUCTURE  → Infraestrutura
COMMUNICATION   → Comunicação
COMPLIANCE      → Conformidade
OTHER           → Outro
```

---

## 5. State Management (NgRx)

### 5.1 `IncidentState`

```typescript
interface IncidentState {
  incidents: IncidentRecord[];           // lista completa (sem paginação)
  classification: IncidentClassification | null;
  loading: boolean;
  error: string | null;
}
```

> Sem `currentIncident` nem `pagination` — o backend não tem GET por ID nem paginação.

### 5.2 Actions (nomenclatura obrigatória)

```
[Incident Form] Classify Incident
[Incident API]  Classify Incident Success
[Incident API]  Classify Incident Error

[Incident History] Load Incidents
[Incident API]     Load Incidents Success
[Incident API]     Load Incidents Error

[Incident] Clear Classification
[Incident] Clear Error
```

> ⚠️ Não gerar `Load Incident By Id` — endpoint não existe no backend.

### 5.3 Effects

- `classifyIncident$` → `switchMap` → `POST /incidentClassifier` → `timeout(30000)` → retry exponencial (max 3, delay 5 s / 10 s / 20 s) somente para `status === 0` (sem conexão)
- `loadIncidents$` → `switchMap` → `GET /incidentClassifier` → `timeout(5000)` → sem retry
- Em cada effect de sucesso: `NotificationService.success(...)`
- Em cada effect de erro: `NotificationService.error(...)`

---

## 6. Serviços Core

### 6.1 `IncidentService`

```typescript
// POST /incidentClassifier — retorna IncidentClassification direto (200 OK, sem wrapper)
classifyIncident(request: IncidentRequest): Observable<IncidentClassification>

// GET /incidentClassifier — retorna IncidentRecord[] direto (array sem wrapper, sem paginação)
listIncidents(): Observable<IncidentRecord[]>
```

> Não adicionar `getIncidentById` — endpoint não existe.  
> Não adicionar lógica de retry aqui — responsabilidade do Effect.

### 6.2 `HttpClientService`

Wrapper sobre `HttpClient` com:
- `timeout(30000)` para POST, `timeout(5000)` para GET
- Sem `retry()` automático — deixar o Effect decidir
- `catchError` relança o `HttpErrorResponse` original para o Effect tratar

### 6.3 `NotificationService`

Usa `MatSnackBar`:
- `success(message)` → `panelClass: 'notification-success'`, duração 3 s
- `error(message)` → `panelClass: 'notification-error'`, duração 5 s

---

## 7. Interceptors (Functional — Angular 17)

> ⚠️ **Não gerar `authInterceptor`** — o backend não tem autenticação implementada.

### 7.1 `errorInterceptor`

Captura `HttpErrorResponse`, mapeia para mensagem pt-BR conforme tabela da seção 3.1, chama `NotificationService.error(...)`, re-lança o erro.

```typescript
// Mapeamento real baseado no GlobalExceptionHandler do backend
status 400  → exibir error.error.message (mensagem do campo inválido)
status 422  → "Falha ao classificar o incidente. Tente novamente."
status 500  → "Erro no servidor. Tente novamente."
status 0    → "Erro de conexão. Verifique sua internet."
```

### 7.2 `loadingInterceptor`

Despacha `UiActions.setLoading({ isLoading: true })` no início e `setLoading({ isLoading: false })` no `finalize`.

---

## 8. Guards

> ⚠️ **O backend não tem autenticação.** Não gerar `authGuard` nem `roleBasedGuard`.  
> Rotas são públicas nesta versão.

---

## 9. Roteamento

```
/                       → redirect → /incident
/incident               → IncidentFormPage (lazy)
/incident/history       → IncidentHistoryPage (lazy)
/**                     → redirect → /incident
```

> ⚠️ Não gerar `/incident/:id` — GET por ID não existe no backend.  
> Todos os feature modules via `loadComponent()` com `import(...)`.  
> Sem guards de autenticação.

---

## 10. Shared Components

### 10.1 `LoadingSpinnerComponent`

```typescript
@Input() isLoading: boolean = false;
@Input() message: string = '';
@Input() diameter: number = 50;
```

Exibe `mat-spinner` centralizado. Usa `*ngIf`.

### 10.2 `ErrorAlertComponent`

```typescript
@Input() message: string = '';
@Input() show: boolean = false;
```

Usa `mat-card` com cor `warn` e ícone `error_outline`.

### 10.3 `IncidentCategoryBadgeComponent` (feature shared)

```typescript
@Input() category!: IncidentCategory;
```

Exibe label traduzido em `mat-chip` com cor primária.

### 10.4 `IncidentSeverityBadgeComponent` (feature shared)

```typescript
@Input() severity!: IncidentSeverity;
```

Exibe `mat-chip` com cor conforme tabela da seção 4.5.

---

## 11. Estratégia RxJS

| Padrão | Uso obrigatório |
|--------|-----------------|
| `takeUntil(this.destroy$)` | Em toda subscription manual |
| `async` pipe no template | Para observables de store e services |
| `shareReplay(1)` | Em observables reutilizados dentro de component |
| `Subject<void>` destroy$ | Em todo component com subscription manual |
| `debounceTime(300)` | Em campos de busca/filtro |

```typescript
// Padrão base para todo component com subscription manual
private destroy$ = new Subject<void>();

ngOnDestroy(): void {
  this.destroy$.next();
  this.destroy$.complete();
}
```

---

## 12. Angular Material — Componentes Obrigatórios

| Funcionalidade | Componente Material |
|----------------|---------------------|
| Formulário     | `mat-form-field`, `mat-label`, `mat-error`, `mat-hint`, `matInput` |
| Textarea       | `<textarea matInput cdkTextareaAutosize>` |
| Botões         | `mat-raised-button`, `mat-stroked-button`, `mat-icon-button` |
| Loading        | `mat-progress-spinner`, `mat-progress-bar` |
| Tabela         | `mat-table`, `mat-sort` (sem `mat-paginator` — sem paginação no backend) |
| Badges         | `mat-chip-listbox`, `mat-chip` |
| Notificações   | `MatSnackBar` |
| Cards          | `mat-card`, `mat-card-header`, `mat-card-content` |
| Ícones         | `mat-icon` (Material Icons) |
| Toolbar        | `mat-toolbar` |

---

## 13. Responsividade

**Mobile-first.** Breakpoints:

```scss
$bp-tablet:  600px;
$bp-desktop: 960px;
$bp-wide:   1280px;
```

Mixins obrigatórios em `styles/_mixins.scss`:
```scss
@mixin tablet-up { @media (min-width: 600px) { @content; } }
@mixin desktop-up { @media (min-width: 960px) { @content; } }
@mixin mobile-only { @media (max-width: 599px) { @content; } }
```

Layout esperado:
- **Mobile:** coluna única, sidebar oculta (hambúrguer)
- **Tablet:** sidebar colapsada, formulário ocupa 100%
- **Desktop:** sidebar fixa 240 px, conteúdo restante

Formulário de ocorrência: textarea com `min-height: 160px`, ocupa 100% da largura disponível.  
Tabela de histórico: scroll horizontal em mobile (`overflow-x: auto`).

---

## 14. Estilização

- **SCSS** em todos os components (scoped)
- **BEM** para classes locais
- Variáveis globais em `src/styles/_variables.scss`
- Tema Material em `src/styles/theme.scss` (palette azul primária, rosa accent)
- **Não usar** `::ng-deep` exceto para customizar Material internamente
- Cor de fundo do app: `#f5f5f5` (cinza claro)
- Cores de severidade definidas como variáveis SCSS:
  ```scss
  $severity-low:    #28a745;
  $severity-medium: #ffc107;
  $severity-high:   #dc3545;
  ```

---

## 15. `app.config.ts` — Providers Obrigatórios

```typescript
provideRouter(appRoutes, withComponentInputBinding()),
provideHttpClient(withInterceptors([errorInterceptor, loadingInterceptor])), // sem authInterceptor
provideAnimations(),
provideStore({ incident: incidentReducer, ui: uiReducer }),
provideEffects([IncidentEffects]),
provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() })
```

---

## 16. Environments

`src/environments/environment.ts`:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'  // sem prefixo /api — backend usa /incidentClassifier direto
};
```

`src/environments/environment.prod.ts`:
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.sico.com/api'
};
```

---

## 17. `tsconfig.json` — Configuração Obrigatória

```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    "strictPropertyInitialization": true,
    "noImplicitReturns": true,
    "forceConsistentCasingInFileNames": true
  }
}
```

---

## 18. Convenções de Nomes

| Artefato       | Formato                        | Exemplo                          |
|----------------|--------------------------------|----------------------------------|
| Component      | `kebab-case.component.ts`      | `incident-form.component.ts`     |
| Service        | `kebab-case.service.ts`        | `incident.service.ts`            |
| Interceptor    | `kebab-case.interceptor.ts`    | `error.interceptor.ts`           |
| Model          | `kebab-case.model.ts`          | `incident.model.ts`              |
| State          | `kebab-case.state.ts`          | `incident.state.ts`              |
| Actions        | `kebab-case.actions.ts`        | `incident.actions.ts`            |
| Seletores NgRx | `select[Feature][Property]`    | `selectIncidents`                |
| Observables    | sufixo `$`                     | `loading$`, `incidents$`         |
| Booleans       | prefixo `is` / `has` / `can`   | `isLoading`, `hasError`          |

---

## 19. Loading States — Regras de Exibição

| Situação                        | O que exibir                           |
|---------------------------------|----------------------------------------|
| POST em andamento               | Spinner centralizado + texto "Classificando..." + botão desabilitado |
| GET da lista em andamento       | `mat-progress-bar` no topo da tabela   |
| Erro de API (400/422/500)       | `ErrorAlertComponent` + snackbar       |
| Lista vazia (`[]`)              | Mensagem "Nenhum incidente registrado." com ícone |

---

## 20. O Que Gerar (Checklist)

Produza os arquivos nesta ordem:

1. `tsconfig.json` e `tsconfig.app.json` (strict mode + paths)
2. `package.json` com dependências: `@angular/core@17`, `@angular/material@17`, `@ngrx/store`, `@ngrx/effects`, `@ngrx/store-devtools`, `rxjs`
3. `src/environments/environment.ts` e `environment.prod.ts` (apiUrl sem `/api`)
4. `src/styles/` — `_variables.scss`, `_mixins.scss`, `theme.scss`, `global.scss`
5. `core/models/incident.model.ts` (sem `api-response.model.ts` — backend não usa wrapper)
6. `core/services/http-client.service.ts`
7. `core/services/incident.service.ts` (métodos: `classifyIncident`, `listIncidents`)
8. `core/services/notification.service.ts`
9. `core/interceptors/error.interceptor.ts` (sem authInterceptor)
10. `core/interceptors/loading.interceptor.ts`
11. `core/store/ui/` — state, actions, reducer
12. `core/store/incident/` — state, actions, reducer, selectors, effects
13. `shared/components/loading-spinner/`
14. `shared/components/error-alert/`
15. `shared/pipes/format-date.pipe.ts`
16. `modules/incident/components/incident-category-badge/`
17. `modules/incident/components/incident-severity-badge/`
18. `modules/incident/pages/incident-form/` (form + result inline após classificação)
19. `modules/incident/pages/incident-history/` (tabela sem paginação, com `mat-sort`)
20. `modules/incident/incident.routes.ts`
21. `app/app.component.ts` (shell: toolbar + router-outlet)
22. `app/app.routes.ts`
23. `app/app.config.ts` (sem authGuard, sem authInterceptor)
24. `src/main.ts`
25. `src/index.html`
26. `angular.json`

---

## 21. Restrições

- **Não** usar `NgModule` — apenas standalone components e `app.config.ts`
- **Não** usar `any` em TypeScript — usar `unknown` quando necessário
- **Não** fazer chamadas HTTP diretamente em components — sempre via Effects/Services
- **Não** subscrever manualmente em templates — usar `async` pipe
- **Não** adicionar autenticação — o backend não tem auth; não gerar `authGuard`, `authInterceptor`, `AuthService`, `/login`
- **Não** criar backend — integrar com a API REST já existente (seção 3)
- **Não** adicionar testes unitários nesta iteração — apenas o código de produção
- **Não** adicionar comentários no código exceto quando o `WHY` for não óbvio

---

## 22. Exemplo de Output Esperado para `incident-form.component.ts`

```typescript
@Component({
  selector: 'app-incident-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatCardModule,
    IncidentCategoryBadgeComponent,
    IncidentSeverityBadgeComponent,
    LoadingSpinnerComponent,
    ErrorAlertComponent,
    FormatDatePipe
  ],
  templateUrl: './incident-form.component.html',
  styleUrl: './incident-form.component.scss'
})
export class IncidentFormComponent implements OnInit, OnDestroy {
  form: FormGroup;
  loading$: Observable<boolean>;
  classification$: Observable<IncidentClassification | null>;
  error$: Observable<string | null>;

  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private store: Store) {
    this.form = this.createForm();
    this.loading$ = this.store.select(selectIncidentLoading);
    this.classification$ = this.store.select(selectClassification);
    this.error$ = this.store.select(selectIncidentError);
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get charCount(): number {
    return this.form.get('incident')?.value?.length ?? 0;
  }

  private createForm(): FormGroup {
    return this.fb.group({
      // Validação de tamanho APENAS no frontend — backend só valida @NotBlank
      incident: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(5000)]]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const request: IncidentRequest = {
        incident: this.form.get('incident')!.value  // campo 'incident', não 'occurrence'
      };
      this.store.dispatch(incidentActions.classifyIncident({ request }));
    }
  }

  onNewIncident(): void {
    this.form.reset();
    this.store.dispatch(incidentActions.clearClassification());
  }
}
```

---

---

## 23. Angular Rules (Regras para Claude)

> Estas regras são **obrigatórias** e se aplicam a **todo** código Angular gerado neste projeto.  
> Violações devem ser corrigidas antes de entregar o arquivo.

---

### 23.1 Standalone Components — Obrigatório

```
✅ Sempre usar standalone: true em todos os @Component
✅ Declarar imports explicitamente no @Component decorator
✅ Nunca usar NgModule para features novas
✅ Prover serviços via providedIn: 'root' ou no array providers do app.config.ts
```

```typescript
// ✅ CORRETO
@Component({
  selector: 'app-foo',
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  template: `...`
})

// ❌ ERRADO — não usar NgModule
@NgModule({
  declarations: [FooComponent],
  imports: [CommonModule]
})
```

---

### 23.2 TypeScript Strict — Obrigatório

```
✅ Sem 'any' — usar tipos explícitos ou 'unknown'
✅ Non-null assertion (!) só quando a ausência é impossível
✅ Optional chaining (?.) sempre que o valor pode ser null/undefined
✅ Interfaces para DTOs, classes para modelos com lógica
✅ Enums para valores fechados (category, severity, status)
✅ Generics em services e stores: Observable<T>, Signal<T>
```

```typescript
// ✅ CORRETO
classifyOccurrence(request: OccurrenceRequest): Observable<OccurrenceClassification>
const value = this.form.get('occurrence')?.value ?? '';

// ❌ ERRADO
classifyOccurrence(request: any): Observable<any>
const value = this.form.get('occurrence')!.value;
```

---

### 23.3 Change Detection — Obrigatório

```
✅ ChangeDetectionStrategy.OnPush em todos os components
✅ Usar async pipe para consumir Observables no template
✅ Usar Signal<T> quando o valor muda por interação local (Angular 17+)
✅ Evitar detectChanges() manual — preferir reactive patterns
```

```typescript
// ✅ CORRETO
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<div *ngIf="loading$ | async">...</div>`
})

// ❌ ERRADO — subscription manual que força CD Default
this.loading$.subscribe(v => this.isLoading = v);
```

---

### 23.4 RxJS — Padrões Obrigatórios

```
✅ takeUntil(this.destroy$) em toda subscription manual
✅ switchMap para requisições canceláveis (busca, classificação)
✅ mergeMap para operações paralelas independentes
✅ shareReplay(1) em observables consumidos múltiplas vezes no template
✅ catchError sempre dentro do pipe do Effect, nunca no component
✅ Nunca criar subscription dentro de subscription (aninhamento)
✅ Preferir async pipe a subscribe() no component
```

```typescript
// ✅ CORRETO — Effect com tratamento completo
classifyOccurrence$ = createEffect(() =>
  this.actions$.pipe(
    ofType(occurrenceActions.classifyOccurrence),
    switchMap(({ request }) =>
      this.service.classifyOccurrence(request).pipe(
        map(classification => occurrenceActions.classifyOccurrenceSuccess({ classification })),
        catchError(error => of(occurrenceActions.classifyOccurrenceError({ error: error.message })))
      )
    )
  )
);

// ❌ ERRADO — HTTP dentro de component
ngOnInit(): void {
  this.service.classifyOccurrence(req).subscribe(result => {
    this.result = result; // mutação direta
  });
}
```

---

### 23.5 NgRx — Padrões Obrigatórios

```
✅ createActionGroup ou createAction com props<{}>() — sem propriedades extras
✅ Selectors com createSelector — nunca acessar store.select((s) => s.feature.prop) inline
✅ Reducers puros — sem side effects, sem mutations, sem chamadas HTTP
✅ Effects para todo side effect: HTTP, navegação, storage, notificação
✅ Facade service opcional para isolar store de components complexos
✅ Estado inicial tipado como const initialState: FeatureState
```

```typescript
// ✅ CORRETO — selector reutilizável
export const selectOccurrenceLoading = createSelector(
  selectOccurrenceState,
  (state) => state.loading
);

// ❌ ERRADO — lógica no template
loading$ = this.store.select(state => state['occurrence']['loading']);
```

---

### 23.6 Formulários Reativos — Obrigatório

```
✅ Sempre ReactiveFormsModule — nunca FormsModule (template-driven)
✅ FormBuilder injetado via constructor
✅ Validators compostos no array: [required, minLength(10), maxLength(5000)]
✅ Accessor via form.get('campo')?.value (com optional chaining)
✅ Desabilitar botão com [disabled]="form.invalid || (loading$ | async)"
✅ mat-error para cada tipo de validação com *ngIf
```

```html
<!-- ✅ CORRETO -->
<mat-form-field>
  <mat-label>Ocorrência</mat-label>
  <textarea matInput formControlName="occurrence" rows="6"></textarea>
  <mat-hint align="end">{{ charCount }} / 5000</mat-hint>
  <mat-error *ngIf="form.get('occurrence')?.hasError('required')">
    Campo obrigatório
  </mat-error>
  <mat-error *ngIf="form.get('occurrence')?.hasError('minlength')">
    Mínimo 10 caracteres
  </mat-error>
  <mat-error *ngIf="form.get('occurrence')?.hasError('maxlength')">
    Máximo 5000 caracteres
  </mat-error>
</mat-form-field>
```

---

### 23.7 Angular Material — Padrões

```
✅ Importar apenas os módulos Material usados no component (não importar tudo)
✅ Usar mat-raised-button para ação primária, mat-stroked-button para secundária
✅ Usar mat-icon com nomes do Material Icons (não SVG inline)
✅ Usar MatSnackBar via NotificationService — nunca diretamente no component
✅ Usar MatDialog via método de serviço — nunca abrir direto do template
✅ Respeitar theming: color="primary" | "accent" | "warn"
```

```typescript
// ✅ CORRETO — importar apenas o necessário
imports: [MatButtonModule, MatFormFieldModule, MatInputModule]

// ❌ ERRADO — importar tudo
imports: [MaterialModule] // módulo customizado genérico
```

---

### 23.8 Injeção de Dependência — Angular 17

```
✅ Preferir inject() em guards funcionais e interceptors funcionais
✅ constructor() para injeção em classes (services, components)
✅ providedIn: 'root' para serviços singleton
✅ Nunca usar @Inject(TOKEN) para tokens padrão — usar inject(MyService)
```

```typescript
// ✅ CORRETO — guard funcional com inject()
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  return authService.isAuthenticated().pipe(
    take(1),
    map(isAuth => isAuth || router.createUrlTree(['/login']))
  );
};

// ✅ CORRETO — component com constructor
constructor(
  private fb: FormBuilder,
  private store: Store
) {}
```

---

### 23.9 Template — Boas Práticas

```
✅ trackBy em todo *ngFor que renderiza listas de objetos
✅ ng-container para agrupamentos sem elemento DOM extra
✅ Interpolação {{ }} apenas para strings simples — property binding para o resto
✅ Evitar lógica complexa no template — mover para computed/getter no component
✅ Usar @if / @for (Angular 17 control flow) quando o projeto aceitar — ou *ngIf/*ngFor
```

```html
<!-- ✅ CORRETO — trackBy obrigatório -->
<mat-row *matRowDef="let row; columns: displayedColumns;"
         [routerLink]="['/occurrence', row.id]">
</mat-row>

<!-- Em ngFor manual -->
<div *ngFor="let item of items; trackBy: trackById">{{ item.label }}</div>
```

```typescript
// trackBy sempre no component
trackById(_index: number, item: OccurrenceRecord): string {
  return item.id;
}
```

---

### 23.10 SCSS / Estilização

```
✅ Usar variáveis SCSS ($color-primary, $spacing-md) — nunca valores hardcoded
✅ BEM para classes locais: .block__element--modifier
✅ Encapsulamento padrão (ViewEncapsulation.Emulated) — nunca None
✅ ::ng-deep apenas para customização de componentes Material (e documentar o motivo)
✅ Mixins para breakpoints — nunca media query inline repetida
✅ Sem !important — reorganizar especificidade se necessário
```

```scss
// ✅ CORRETO
.occurrence-form {
  &__textarea {
    width: 100%;
    min-height: 160px;
  }

  &__counter {
    font-size: 12px;
    color: mat.get-color-from-palette($primary, 500);
    text-align: right;
  }

  @include mobile-only {
    padding: $spacing-sm;
  }
}

// ❌ ERRADO
.myform textarea {
  width: 100% !important;
  min-height: 160px;
  color: #1976d2; // hardcoded
}
```

---

### 23.11 Routing — Angular 17

```
✅ Lazy loading obrigatório para todos os feature modules
✅ loadComponent() para rotas de component único
✅ loadChildren() para rotas com sub-rotas
✅ withComponentInputBinding() no provideRouter para receber params como @Input
✅ Guards funcionais (CanActivateFn) — nunca classe com CanActivate
✅ data: { title: '...' } em cada rota para breadcrumbs/título da página
```

```typescript
// ✅ CORRETO — lazy com loadComponent
{
  path: 'history',
  loadComponent: () =>
    import('./pages/occurrence-history/occurrence-history.component')
      .then(m => m.OccurrenceHistoryComponent),
  data: { title: 'Histórico de Ocorrências' }
}
```

---

### 23.12 Nomenclatura — Regras Finais

```
Selectors NgRx : select[Feature][Property]       → selectOccurrenceLoading
Actions NgRx   : [Feature Source] Event Name     → [Occurrence Form] Classify Occurrence
Effects        : camelCase$                       → classifyOccurrence$
Observables    : sufixo $                         → loading$, occurrences$
Subjects       : sufixo $                         → destroy$
Booleans       : prefixo is/has/can/should        → isLoading, hasError
Event handlers : prefixo on                       → onSubmit(), onDelete()
Private fields : prefixo _                        → _subscription (apenas em classes base)
```

---

*Gerado em: 2026-05-19 | Versão: 1.0 | Projeto: SICO*
