# Documento de Arquitetura Frontend Enterprise
## Angular 17 — Sistema Inteligente de Registro e Classificação de Ocorrências

**Versão:** 1.0  
**Data:** Maio 2026  
**Status:** Aprovado  
**Classificação:** Interno  
**Autor:** Equipe de Arquitetura Frontend  
**Revisores:** [Arquitetos de Software, Tech Leads]

---

## 1. Introdução

### 1.1 Objetivo
Este documento especifica a arquitetura frontend enterprise para o **Sistema Inteligente de Registro e Classificação de Ocorrências (SICO)**, utilizando **Angular 17** com standalone components, state management reativo e padrões escaláveis.

### 1.2 Públicos-Alvo
- Arquitetos de Software Frontend
- Desenvolvedores Angular Senior/Mid-level
- Tech Leads de Frontend
- Engenheiros de DevOps
- Analistas de QA/Testes

### 1.3 Princípios Arquiteturais

| Princípio | Descrição |
|-----------|-----------|
| **Escalabilidade** | Arquitetura suporta crescimento sem necessidade de refatoração maior |
| **Desacoplamento** | Camadas isoladas com responsabilidades bem-definidas |
| **Reusabilidade** | Componentes, serviços e utilitários reutilizáveis |
| **Type-Safety** | TypeScript strict mode com tipos bem-definidos |
| **Reatividade** | RxJS como backbone de fluxo de dados |
| **Testabilidade** | Componentes e serviços facilmente testáveis |
| **Manutenibilidade** | Código claro, bem documentado e seguindo convenções |
| **Performance** | Otimizações de carregamento, detecção de mudanças, bundle |

---

## 2. Estrutura de Pastas e Organização

### 2.1 Estrutura Geral

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                          # Singleton services, interceptors, guards
│   │   │   ├── guards/
│   │   │   │   ├── auth.guard.ts
│   │   │   │   ├── unsaved-changes.guard.ts
│   │   │   │   └── role-based.guard.ts
│   │   │   ├── interceptors/
│   │   │   │   ├── auth.interceptor.ts
│   │   │   │   ├── error.interceptor.ts
│   │   │   │   └── loading.interceptor.ts
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts
│   │   │   │   ├── incident.service.ts
│   │   │   │   ├── http-client.service.ts
│   │   │   │   └── notification.service.ts
│   │   │   ├── models/
│   │   │   │   ├── incident.model.ts
│   │   │   │   ├── user.model.ts
│   │   │   │   └── api-response.model.ts
│   │   │   ├── store/                     # NgRx/Signals state management
│   │   │   │   ├── incident/
│   │   │   │   │   ├── incident.state.ts
│   │   │   │   │   ├── incident.actions.ts
│   │   │   │   │   ├── incident.reducer.ts
│   │   │   │   │   ├── incident.selectors.ts
│   │   │   │   │   └── incident.effects.ts
│   │   │   │   ├── ui/
│   │   │   │   │   ├── ui.state.ts
│   │   │   │   │   ├── ui.actions.ts
│   │   │   │   │   └── ui.reducer.ts
│   │   │   │   └── app-store.config.ts
│   │   │   └── core.config.ts
│   │   │
│   │   ├── shared/                        # Componentes, pipes, diretivas compartilhadas
│   │   │   ├── components/
│   │   │   │   ├── header/
│   │   │   │   │   ├── header.component.ts
│   │   │   │   │   ├── header.component.html
│   │   │   │   │   └── header.component.scss
│   │   │   │   ├── footer/
│   │   │   │   ├── sidebar/
│   │   │   │   ├── loading-spinner/
│   │   │   │   ├── error-alert/
│   │   │   │   ├── confirmation-dialog/
│   │   │   │   └── shared-components.ts   # Barrel export
│   │   │   ├── pipes/
│   │   │   │   ├── safe-html.pipe.ts
│   │   │   │   ├── capitalize.pipe.ts
│   │   │   │   └── format-date.pipe.ts
│   │   │   ├── directives/
│   │   │   │   ├── highlight.directive.ts
│   │   │   │   ├── debounce-click.directive.ts
│   │   │   │   └── app-permission.directive.ts
│   │   │   ├── models/
│   │   │   │   ├── pagination.model.ts
│   │   │   │   ├── api-error.model.ts
│   │   │   │   └── notification.model.ts
│   │   │   ├── utils/
│   │   │   │   ├── validators.util.ts
│   │   │   │   ├── date-helpers.util.ts
│   │   │   │   ├── format-helpers.util.ts
│   │   │   │   └── storage.util.ts
│   │   │   └── shared.config.ts
│   │   │
│   │   ├── modules/                       # Feature modules (lazy-loaded)
│   │   │   ├── incident/
│   │   │   │   ├── incident.routes.ts
│   │   │   │   ├── pages/
│   │   │   │   │   ├── incident-form/
│   │   │   │   │   │   ├── incident-form.component.ts
│   │   │   │   │   │   ├── incident-form.component.html
│   │   │   │   │   │   ├── incident-form.component.scss
│   │   │   │   │   │   └── incident-form.component.spec.ts
│   │   │   │   │   ├── incident-result/
│   │   │   │   │   ├── incident-history/
│   │   │   │   │   └── incident-detail/
│   │   │   │   ├── components/
│   │   │   │   │   ├── incident-category-badge/
│   │   │   │   │   ├── incident-severity-badge/
│   │   │   │   │   ├── incident-table/
│   │   │   │   │   └── incident-card/
│   │   │   │   ├── services/
│   │   │   │   │   └── incident-facade.service.ts
│   │   │   │   ├── store/                 # Local feature store (ngRx)
│   │   │   │   │   ├── incident-form.state.ts
│   │   │   │   │   ├── incident-form.actions.ts
│   │   │   │   │   ├── incident-form.reducer.ts
│   │   │   │   │   └── incident-form.selectors.ts
│   │   │   │   ├── models/
│   │   │   │   │   ├── incident-form.model.ts
│   │   │   │   │   └── incident-dto.ts
│   │   │   │   └── incident.config.ts
│   │   │   │
│   │   │   └── admin/
│   │   │       ├── admin.routes.ts
│   │   │       ├── pages/
│   │   │       │   ├── dashboard/
│   │   │       │   └── user-management/
│   │   │       ├── components/
│   │   │       ├── services/
│   │   │       └── admin.config.ts
│   │   │
│   │   ├── app.routes.ts                  # Configuração de rotas
│   │   ├── app.config.ts                  # Configuração global da aplicação
│   │   ├── app.component.ts               # Root component
│   │   └── app.component.html
│   │
│   ├── assets/
│   │   ├── images/
│   │   ├── icons/
│   │   ├── animations/
│   │   └── styles/
│   │       ├── variables.scss
│   │       ├── mixins.scss
│   │       ├── typography.scss
│   │       └── responsive.scss
│   │
│   ├── environments/
│   │   ├── environment.ts
│   │   ├── environment.prod.ts
│   │   └── environment.staging.ts
│   │
│   ├── styles/
│   │   ├── global.scss
│   │   ├── theme.scss
│   │   └── breakpoints.scss
│   │
│   ├── main.ts                            # Bootstrap da aplicação
│   ├── index.html
│   └── favicon.ico
│
├── angular.json                           # Configuração Angular CLI
├── tsconfig.json                          # TypeScript config (strict mode)
├── tsconfig.app.json
├── tsconfig.spec.json
├── karma.conf.js                          # Teste unitário
├── package.json
├── package-lock.json
└── README.md

```

### 2.2 Regras de Organização

**RFA-ORG-01:** Cada feature module segue a estrutura: `pages/`, `components/`, `services/`, `models/`, `store/`

**RFA-ORG-02:** Components standalone são preferidos; módulos NgModule apenas quando necessário

**RFA-ORG-03:** Imports são explícitos; não usar `barrel exports` em `index.ts` para evitar ciclos

**RFA-ORG-04:** Paths aliases na `tsconfig.json` para melhorar legibilidade:
```json
{
  "compilerOptions": {
    "baseUrl": ".",
    "paths": {
      "@app/*": ["src/app/*"],
      "@core/*": ["src/app/core/*"],
      "@shared/*": ["src/app/shared/*"],
      "@modules/*": ["src/app/modules/*"],
      "@assets/*": ["src/assets/*"],
      "@environments/*": ["src/environments/*"]
    }
  }
}
```

---

## 3. Organização de Módulos

### 3.1 Estrutura de Módulos em Angular 17

**RFA-MOD-01:** Utilizar **standalone components** como padrão (Angular 17+)

**RFA-MOD-02:** Modularização por feature:
- `AppComponent` (root)
- `CoreModule` — lógica singleton (lazy em appConfig)
- `SharedModule` — componentes, pipes, diretivas compartilhadas
- Feature modules — lazy-loaded via routes

### 3.2 AppComponent

```typescript
// app.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '@shared/components/header/header.component';
import { SidebarComponent } from '@shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, SidebarComponent],
  template: `
    <app-header></app-header>
    <div class="main-container">
      <app-sidebar></app-sidebar>
      <main class="content">
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  ngOnInit() {
    // Inicialização global
  }

  ngOnDestroy() {
    // Limpeza global
  }
}
```

### 3.3 app.config.ts

```typescript
// app.config.ts
import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { appRoutes } from './app.routes';
import { authInterceptor } from '@core/interceptors/auth.interceptor';
import { errorInterceptor } from '@core/interceptors/error.interceptor';
import { coreReducers } from '@core/store/app-store.config';
import { CoreEffects } from '@core/store/core.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(appRoutes),
    provideHttpClient(
      withInterceptors([authInterceptor, errorInterceptor])
    ),
    provideAnimations(),
    provideStore(coreReducers),
    provideEffects([CoreEffects])
  ]
};
```

### 3.4 app.routes.ts

```typescript
// app.routes.ts
import { Routes } from '@angular/router';
import { authGuard } from '@core/guards/auth.guard';
import { AppComponent } from './app.component';

export const appRoutes: Routes = [
  {
    path: '',
    component: AppComponent,
    children: [
      {
        path: '',
        redirectTo: 'incident',
        pathMatch: 'full'
      },
      {
        path: 'incident',
        canActivate: [authGuard],
        loadChildren: () => 
          import('@modules/incident/incident.routes')
            .then(m => m.incidentRoutes)
      },
      {
        path: 'admin',
        canActivate: [authGuard],
        canActivateChild: [roleBasedGuard('ADMIN')],
        loadChildren: () => 
          import('@modules/admin/admin.routes')
            .then(m => m.adminRoutes)
      },
      {
        path: '**',
        redirectTo: 'incident'
      }
    ]
  }
];
```

---

## 4. Standalone Components

### 4.1 Padrão de Standalone Components

**RFA-SC-01:** Todos os novos componentes são standalone (exceto casos legados)

**RFA-SC-02:** Imports explícitos no decorator `@Component`

**RFA-SC-03:** CommonModule importado quando necessários `*ngIf`, `*ngFor`, etc.

### 4.2 Exemplo: IncidentFormComponent (Standalone)

```typescript
// incident-form.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { IncidentFormActions } from '../store/incident-form.actions';
import { selectFormLoading } from '../store/incident-form.selectors';

@Component({
  selector: 'app-incident-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './incident-form.component.html',
  styleUrl: './incident-form.component.scss'
})
export class IncidentFormComponent implements OnInit, OnDestroy {
  form: FormGroup;
  loading$ = this.store.select(selectFormLoading);
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private store: Store
  ) {
    this.form = this.createForm();
  }

  ngOnInit(): void {
    // Inicializações
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      incident: [
        '',
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(5000)
        ]
      ]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const incident = this.form.get('incident')?.value;
      this.store.dispatch(
        IncidentFormActions.submitIncident({ incident })
      );
    }
  }
}
```

### 4.3 Exemplo: SharedComponent (Standalone)

```typescript
// loading-spinner.component.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  template: `
    <div class="spinner-container" *ngIf="isLoading">
      <mat-spinner [diameter]="diameter" [strokeWidth]="strokeWidth"></mat-spinner>
      <p *ngIf="message">{{ message }}</p>
    </div>
  `,
  styles: [`
    .spinner-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      min-height: 200px;
    }
  `]
})
export class LoadingSpinnerComponent {
  @Input() isLoading: boolean = false;
  @Input() message: string = '';
  @Input() diameter: number = 50;
  @Input() strokeWidth: number = 4;
}
```

---

## 5. Shared Module e Components

### 5.1 Estrutura de Shared Components

**RFA-SH-01:** Shared components são reutilizáveis em múltiplas features

**RFA-SH-02:** Componentes compartilhados devem ser agnósticos de domínio (quando possível)

**RFA-SH-03:** Exports declarados em `shared-components.ts` para facilitar imports

### 5.2 shared-components.ts

```typescript
// shared/components/shared-components.ts
export { HeaderComponent } from './header/header.component';
export { FooterComponent } from './footer/footer.component';
export { SidebarComponent } from './sidebar/sidebar.component';
export { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
export { ErrorAlertComponent } from './error-alert/error-alert.component';
export { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
```

### 5.3 Componentes Essenciais

| Componente | Responsabilidade | Standalone |
|------------|-----------------|-----------|
| **HeaderComponent** | Navegação principal, logo, user menu | ✅ Sim |
| **SidebarComponent** | Navegação lateral (mobile: hamburger) | ✅ Sim |
| **FooterComponent** | Rodapé, informações legais | ✅ Sim |
| **LoadingSpinnerComponent** | Indicador de carregamento | ✅ Sim |
| **ErrorAlertComponent** | Exibição de mensagens de erro | ✅ Sim |
| **ConfirmationDialogComponent** | Dialog de confirmação (MatDialog) | ✅ Sim |

---

## 6. Core Module

### 6.1 Responsabilidades do Core

**RFA-CORE-01:** Core contém serviços singleton (instanciados uma única vez)

**RFA-CORE-02:** Core contém guards, interceptors, modelos globais

**RFA-CORE-03:** Core não deve ser importado em feature modules (apenas em appConfig)

### 6.2 Serviços Core

#### 6.2.1 AuthService

```typescript
// core/services/auth.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, catchError, switchMap } from 'rxjs/operators';
import { HttpClientService } from './http-client.service';

export interface User {
  id: string;
  username: string;
  email: string;
  roles: string[];
}

export interface AuthToken {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser$ = new BehaviorSubject<User | null>(null);
  private authToken$ = new BehaviorSubject<AuthToken | null>(null);

  constructor(private http: HttpClientService) {
    this.loadStoredAuth();
  }

  private loadStoredAuth(): void {
    const token = localStorage.getItem('auth_token');
    const user = localStorage.getItem('user');
    if (token && user) {
      this.authToken$.next(JSON.parse(token));
      this.currentUser$.next(JSON.parse(user));
    }
  }

  login(username: string, password: string): Observable<AuthToken> {
    return this.http.post<AuthToken>('/auth/login', { username, password })
      .pipe(
        tap(token => {
          this.authToken$.next(token);
          localStorage.setItem('auth_token', JSON.stringify(token));
        }),
        switchMap(() => this.getUserProfile()),
        catchError(error => {
          console.error('Login failed', error);
          throw error;
        })
      );
  }

  private getUserProfile(): Observable<User> {
    return this.http.get<User>('/auth/profile')
      .pipe(
        tap(user => {
          this.currentUser$.next(user);
          localStorage.setItem('user', JSON.stringify(user));
        })
      );
  }

  logout(): void {
    this.authToken$.next(null);
    this.currentUser$.next(null);
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
  }

  getCurrentUser(): Observable<User | null> {
    return this.currentUser$.asObservable();
  }

  getAuthToken(): Observable<AuthToken | null> {
    return this.authToken$.asObservable();
  }

  isAuthenticated(): Observable<boolean> {
    return this.authToken$.pipe(
      switchMap(token => of(!!token))
    );
  }

  hasRole(role: string): Observable<boolean> {
    return this.currentUser$.pipe(
      switchMap(user => of(user ? user.roles.includes(role) : false))
    );
  }
}
```

#### 6.2.2 IncidentService

```typescript
// core/services/incident.service.ts
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientService } from './http-client.service';
import { IncidentRequest, IncidentClassification, IncidentRecord } from '../models/incident.model';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {
  private apiEndpoint = '/incidentClassifier';

  constructor(private http: HttpClientService) {}

  /**
   * Classifica um novo incidente
   * @param request IncidentRequest com descrição
   * @returns Observable<IncidentClassification>
   */
  classifyIncident(request: IncidentRequest): Observable<IncidentClassification> {
    return this.http.post<IncidentClassification>(
      this.apiEndpoint,
      request
    );
  }

  /**
   * Obtém histórico de incidentes
   * @param skip Número de registros para pular (paginação)
   * @param take Número de registros para retornar
   * @returns Observable<IncidentRecord[]>
   */
  getIncidents(skip: number = 0, take: number = 10): Observable<IncidentRecord[]> {
    return this.http.get<IncidentRecord[]>(
      `${this.apiEndpoint}?skip=${skip}&take=${take}`
    );
  }

  /**
   * Obtém detalhes de um incidente específico
   * @param id ID do incidente
   * @returns Observable<IncidentRecord>
   */
  getIncidentById(id: string): Observable<IncidentRecord> {
    return this.http.get<IncidentRecord>(`${this.apiEndpoint}/${id}`);
  }
}
```

#### 6.2.3 HttpClientService

```typescript
// core/services/http-client.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, timeout, retry } from 'rxjs/operators';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {
  private baseUrl = environment.apiUrl;
  private requestTimeout = 30000; // 30 segundos
  private retryAttempts = 1;

  constructor(private http: HttpClient) {}

  get<T>(endpoint: string, params?: HttpParams): Observable<T> {
    return this.http.get<T>(
      `${this.baseUrl}${endpoint}`,
      { params }
    ).pipe(
      timeout(this.requestTimeout),
      retry(this.retryAttempts),
      catchError(this.handleError.bind(this))
    );
  }

  post<T>(endpoint: string, body: any, params?: HttpParams): Observable<T> {
    return this.http.post<T>(
      `${this.baseUrl}${endpoint}`,
      body,
      { params }
    ).pipe(
      timeout(this.requestTimeout),
      retry(this.retryAttempts),
      catchError(this.handleError.bind(this))
    );
  }

  put<T>(endpoint: string, body: any, params?: HttpParams): Observable<T> {
    return this.http.put<T>(
      `${this.baseUrl}${endpoint}`,
      body,
      { params }
    ).pipe(
      timeout(this.requestTimeout),
      catchError(this.handleError.bind(this))
    );
  }

  delete<T>(endpoint: string, params?: HttpParams): Observable<T> {
    return this.http.delete<T>(
      `${this.baseUrl}${endpoint}`,
      { params }
    ).pipe(
      timeout(this.requestTimeout),
      catchError(this.handleError.bind(this))
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Erro desconhecido';

    if (error.error instanceof ErrorEvent) {
      // Erro no cliente
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      // Erro no servidor
      errorMessage = `Erro ${error.status}: ${error.message}`;
    }

    console.error(errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
```

#### 6.2.4 NotificationService

```typescript
// core/services/notification.service.ts
import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';

export interface Notification {
  message: string;
  type: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private defaultDuration = 3000; // ms

  constructor(
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  /**
   * Exibe notificação de sucesso
   */
  success(message: string, duration: number = this.defaultDuration): void {
    this.show(message, 'success', duration);
  }

  /**
   * Exibe notificação de erro
   */
  error(message: string, duration: number = this.defaultDuration): void {
    this.show(message, 'error', duration);
  }

  /**
   * Exibe notificação de aviso
   */
  warning(message: string, duration: number = this.defaultDuration): void {
    this.show(message, 'warning', duration);
  }

  /**
   * Exibe notificação de informação
   */
  info(message: string, duration: number = this.defaultDuration): void {
    this.show(message, 'info', duration);
  }

  private show(message: string, type: string, duration: number): void {
    const config: MatSnackBarConfig = {
      duration,
      horizontalPosition: 'end',
      verticalPosition: 'bottom',
      panelClass: [`notification-${type}`]
    };

    this.snackBar.open(message, 'Fechar', config);
  }
}
```

---

## 7. Guards (Proteção de Rotas)

### 7.1 AuthGuard

```typescript
// core/guards/auth.guard.ts
import { Injectable } from '@angular/core';
import { Router, CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated().pipe(
    take(1),
    map(isAuth => {
      if (isAuth) {
        return true;
      }
      router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    })
  );
};
```

### 7.2 RoleBasedGuard

```typescript
// core/guards/role-based.guard.ts
import { Injectable } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { map, take } from 'rxjs/operators';

export const roleBasedGuard = (requiredRole: string): CanActivateFn => {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    return authService.hasRole(requiredRole).pipe(
      take(1),
      map(hasRole => {
        if (hasRole) {
          return true;
        }
        router.navigate(['/unauthorized']);
        return false;
      })
    );
  };
};
```

### 7.3 UnsavedChangesGuard

```typescript
// core/guards/unsaved-changes.guard.ts
import { CanDeactivateFn } from '@angular/router';
import { Injectable } from '@angular/core';

export interface CanComponentDeactivate {
  canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable({
  providedIn: 'root'
})
export class CanDeactivateGuard {
  canDeactivate: CanDeactivateFn<CanComponentDeactivate> = (component) => {
    return component.canDeactivate ? component.canDeactivate() : true;
  };
}
```

---

## 8. Interceptors (Middleware HTTP)

### 8.1 AuthInterceptor

```typescript
// core/interceptors/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { switchMap } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  return authService.getAuthToken().pipe(
    switchMap(token => {
      if (token) {
        const clonedReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token.accessToken}`
          }
        });
        return next(clonedReq);
      }
      return next(req);
    })
  );
};
```

### 8.2 ErrorInterceptor

```typescript
// core/interceptors/error.interceptor.ts
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { NotificationService } from '../services/notification.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Erro na requisição';

      if (error.status === 401) {
        errorMessage = 'Sessão expirada. Faça login novamente.';
        router.navigate(['/login']);
      } else if (error.status === 403) {
        errorMessage = 'Acesso negado.';
      } else if (error.status === 404) {
        errorMessage = 'Recurso não encontrado.';
      } else if (error.status === 500) {
        errorMessage = 'Erro no servidor. Tente novamente.';
      } else if (error.status === 0) {
        errorMessage = 'Erro de conexão. Verifique sua internet.';
      }

      notificationService.error(errorMessage);
      return throwError(() => error);
    })
  );
};
```

### 8.3 LoadingInterceptor

```typescript
// core/interceptors/loading.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { finalize } from 'rxjs/operators';
import { UiActions } from '../store/ui/ui.actions';

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);

  store.dispatch(UiActions.setLoading({ isLoading: true }));

  return next(req).pipe(
    finalize(() => {
      store.dispatch(UiActions.setLoading({ isLoading: false }));
    })
  );
};
```

---

## 9. Models e DTOs

### 9.1 Convenção de Nomes

**RFA-MOD-01:** Models (entidades do domínio) terminam em `.model.ts`

**RFA-MOD-02:** DTOs (data transfer objects) terminam em `.dto.ts`

**RFA-MOD-03:** Models são classes tipadas; DTOs são interfaces

### 9.2 Incident Models

```typescript
// core/models/incident.model.ts

export enum IncidentCategory {
  SECURITY = 'SECURITY',
  NETWORK = 'NETWORK',
  HARDWARE = 'HARDWARE',
  SOFTWARE = 'SOFTWARE',
  DATABASE = 'DATABASE',
  DATA_LOSS = 'DATA_LOSS',
  ACCESS_CONTROL = 'ACCESS_CONTROL',
  PERFORMANCE = 'PERFORMANCE',
  SERVICE_OUTAGE = 'SERVICE_OUTAGE',
  INFRASTRUCTURE = 'INFRASTRUCTURE',
  COMMUNICATION = 'COMMUNICATION',
  COMPLIANCE = 'COMPLIANCE',
  OTHER = 'OTHER'
}

export enum IncidentSeverity {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH'
}

/**
 * Request DTO para classificação de incidente
 */
export interface IncidentRequest {
  incident: string; // Descrição do incidente (10-5000 caracteres)
}

/**
 * Response DTO da classificação
 */
export interface IncidentClassification {
  formalizedIncidentText: string;
  category: IncidentCategory;
  severity: IncidentSeverity;
  registeredAt: string; // ISO 8601
  suggestedAction: string;
}

/**
 * Model persistido no banco (histórico)
 */
export class IncidentRecord implements IncidentClassification {
  id?: string;
  originalText: string;
  formalizedIncidentText: string;
  category: IncidentCategory;
  severity: IncidentSeverity;
  registeredAt: string;
  suggestedAction: string;

  constructor(data: Partial<IncidentRecord> = {}) {
    this.id = data.id;
    this.originalText = data.originalText || '';
    this.formalizedIncidentText = data.formalizedIncidentText || '';
    this.category = data.category || IncidentCategory.OTHER;
    this.severity = data.severity || IncidentSeverity.MEDIUM;
    this.registeredAt = data.registeredAt || new Date().toISOString();
    this.suggestedAction = data.suggestedAction || '';
  }
}

/**
 * Model para estado do formulário
 */
export interface IncidentFormState {
  originalText: string;
  classification: IncidentClassification | null;
  error: string | null;
  isLoading: boolean;
  isSubmitted: boolean;
}
```

### 9.3 Common DTOs

```typescript
// shared/models/api-response.model.ts

export interface ApiResponse<T> {
  data: T;
  message: string;
  timestamp: string;
  statusCode: number;
}

export interface ApiError {
  message: string;
  code: string;
  details?: Record<string, any>;
}

export interface PaginationParams {
  skip: number;
  take: number;
  total?: number;
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: PaginationParams;
}
```

---

## 10. State Management (NgRx)

### 10.1 Princípios

**RFA-STATE-01:** NgRx Signals para estado global (performance melhorada)

**RFA-STATE-02:** Actions nomeadas com sufixo descritivo: `Action`, `Success`, `Error`

**RFA-STATE-03:** Selectors para acesso a estado (não acesso direto ao store)

**RFA-STATE-04:** Effects para side effects (chamadas HTTP, navegação, etc.)

### 10.2 Estrutura de Store Global

```
core/store/
├── app-store.config.ts      # Configuração global
├── incident/
│   ├── incident.state.ts
│   ├── incident.actions.ts
│   ├── incident.reducer.ts
│   ├── incident.selectors.ts
│   └── incident.effects.ts
└── ui/
    ├── ui.state.ts
    ├── ui.actions.ts
    └── ui.reducer.ts
```

### 10.3 Example: Incident State

```typescript
// core/store/incident/incident.state.ts
import { IncidentRecord, IncidentClassification } from '@core/models/incident.model';

export interface IncidentState {
  incidents: IncidentRecord[];
  currentIncident: IncidentRecord | null;
  classification: IncidentClassification | null;
  loading: boolean;
  error: string | null;
  pagination: {
    skip: number;
    take: number;
    total: number;
  };
}

export const initialIncidentState: IncidentState = {
  incidents: [],
  currentIncident: null,
  classification: null,
  loading: false,
  error: null,
  pagination: { skip: 0, take: 10, total: 0 }
};
```

```typescript
// core/store/incident/incident.actions.ts
import { createAction, props } from '@ngrx/store';
import { IncidentRequest, IncidentClassification, IncidentRecord } from '@core/models/incident.model';

export const incidentActions = {
  // Classificação de incidente
  classifyIncident: createAction(
    '[Incident Form] Classify Incident',
    props<{ request: IncidentRequest }>()
  ),
  classifyIncidentSuccess: createAction(
    '[Incident API] Classify Incident Success',
    props<{ classification: IncidentClassification }>()
  ),
  classifyIncidentError: createAction(
    '[Incident API] Classify Incident Error',
    props<{ error: string }>()
  ),

  // Carregamento de histórico
  loadIncidents: createAction(
    '[Incident History] Load Incidents',
    props<{ skip: number; take: number }>()
  ),
  loadIncidentsSuccess: createAction(
    '[Incident API] Load Incidents Success',
    props<{ incidents: IncidentRecord[]; total: number }>()
  ),
  loadIncidentsError: createAction(
    '[Incident API] Load Incidents Error',
    props<{ error: string }>()
  ),

  // Carregamento de detalhe
  loadIncidentById: createAction(
    '[Incident Detail] Load Incident By ID',
    props<{ id: string }>()
  ),
  loadIncidentByIdSuccess: createAction(
    '[Incident API] Load Incident By ID Success',
    props<{ incident: IncidentRecord }>()
  ),

  // Limpeza
  clearClassification: createAction(
    '[Incident] Clear Classification'
  ),
  clearError: createAction(
    '[Incident] Clear Error'
  )
};
```

```typescript
// core/store/incident/incident.reducer.ts
import { createReducer, on } from '@ngrx/store';
import { initialIncidentState, IncidentState } from './incident.state';
import { incidentActions } from './incident.actions';

export const incidentReducer = createReducer(
  initialIncidentState,

  // Classify Incident
  on(incidentActions.classifyIncident, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(incidentActions.classifyIncidentSuccess, (state, { classification }) => ({
    ...state,
    classification,
    loading: false
  })),
  on(incidentActions.classifyIncidentError, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Load Incidents
  on(incidentActions.loadIncidents, (state) => ({
    ...state,
    loading: true,
    error: null
  })),
  on(incidentActions.loadIncidentsSuccess, (state, { incidents, total }) => ({
    ...state,
    incidents,
    pagination: { ...state.pagination, total },
    loading: false
  })),
  on(incidentActions.loadIncidentsError, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Clear
  on(incidentActions.clearClassification, (state) => ({
    ...state,
    classification: null
  })),
  on(incidentActions.clearError, (state) => ({
    ...state,
    error: null
  }))
);
```

```typescript
// core/store/incident/incident.selectors.ts
import { createSelector, createFeatureSelector } from '@ngrx/store';
import { IncidentState } from './incident.state';

const selectIncidentState = createFeatureSelector<IncidentState>('incident');

export const selectIncidents = createSelector(
  selectIncidentState,
  (state) => state.incidents
);

export const selectCurrentIncident = createSelector(
  selectIncidentState,
  (state) => state.currentIncident
);

export const selectClassification = createSelector(
  selectIncidentState,
  (state) => state.classification
);

export const selectIncidentLoading = createSelector(
  selectIncidentState,
  (state) => state.loading
);

export const selectIncidentError = createSelector(
  selectIncidentState,
  (state) => state.error
);

export const selectPagination = createSelector(
  selectIncidentState,
  (state) => state.pagination
);
```

```typescript
// core/store/incident/incident.effects.ts
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { IncidentService } from '@core/services/incident.service';
import { NotificationService } from '@core/services/notification.service';
import { incidentActions } from './incident.actions';

@Injectable()
export class IncidentEffects {
  classifyIncident$ = createEffect(() =>
    this.actions$.pipe(
      ofType(incidentActions.classifyIncident),
      switchMap(({ request }) =>
        this.incidentService.classifyIncident(request).pipe(
          map(classification =>
            incidentActions.classifyIncidentSuccess({ classification })
          ),
          tap(() => {
            this.notificationService.success('Incidente classificado com sucesso!');
          }),
          catchError(error =>
            of(incidentActions.classifyIncidentError({ 
              error: error.message || 'Erro ao classificar incidente' 
            }))
          )
        )
      )
    )
  );

  loadIncidents$ = createEffect(() =>
    this.actions$.pipe(
      ofType(incidentActions.loadIncidents),
      switchMap(({ skip, take }) =>
        this.incidentService.getIncidents(skip, take).pipe(
          map(incidents =>
            incidentActions.loadIncidentsSuccess({ 
              incidents, 
              total: incidents.length 
            })
          ),
          catchError(error =>
            of(incidentActions.loadIncidentsError({ 
              error: error.message || 'Erro ao carregar incidentes' 
            }))
          )
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private incidentService: IncidentService,
    private notificationService: NotificationService,
    private store: Store
  ) {}
}
```

---

## 11. Fluxo HTTP

### 11.1 Ciclo de Requisição

```
┌─────────────────────────────────────────┐
│  Component dispara ação (ex: submitForm) │
└────────────────────┬────────────────────┘
                     │
         ┌───────────▼──────────┐
         │   Effects intercepta │
         │   a ação            │
         └───────────┬──────────┘
                     │
        ┌────────────▼─────────┐
        │  Service faz POST    │
        │  (HttpClientService) │
        └────────────┬─────────┘
                     │
        ┌────────────▼──────────┐
        │  Interceptors rodam:  │
        │  - Auth               │
        │  - Loading            │
        │  - Error handling     │
        └────────────┬──────────┘
                     │
        ┌────────────▼──────────┐
        │  Backend retorna      │
        │  response JSON        │
        └────────────┬──────────┘
                     │
        ┌────────────▼──────────┐
        │  Effects recebem      │
        │  Success/Error        │
        └────────────┬──────────┘
                     │
        ┌────────────▼──────────┐
        │  Reducer atualiza     │
        │  o estado             │
        └────────────┬──────────┘
                     │
        ┌────────────▼──────────┐
        │  Component se inscreve│
        │  em selector$          │
        │  (observable)         │
        └────────────┬──────────┘
                     │
        ┌────────────▼──────────┐
        │  Template renderiza   │
        │  com async pipe       │
        └──────────────────────┘
```

### 11.2 Exemplo: Submit de Formulário

```typescript
// Component
onSubmit(): void {
  if (this.form.valid) {
    const request: IncidentRequest = {
      incident: this.form.get('incident')?.value
    };
    this.store.dispatch(incidentActions.classifyIncident({ request }));
  }
}

// Template
<form [formGroup]="form" (ngSubmit)="onSubmit()">
  <mat-form-field>
    <textarea matInput formControlName="incident" placeholder="Descreva o incidente..."></textarea>
  </mat-form-field>
  <button mat-raised-button type="submit" [disabled]="!(form.valid && !(loading$ | async))">
    Classificar
  </button>
</form>

<app-loading-spinner [isLoading]="loading$ | async"></app-loading-spinner>
```

---

## 12. Estratégia RxJS

### 12.1 Princípios

**RFA-RX-01:** Usar `takeUntil` com `destroy$` subject para cleanup em `ngOnDestroy`

**RFA-RX-02:** Preferir `async` pipe no template para evitar subscriptions manuais

**RFA-RX-03:** Usar `shareReplay()` em observables que são consumidos múltiplas vezes

**RFA-RX-04:** Unsubscribe automático com `takeUntil` para evitar memory leaks

### 12.2 Padrão OnDestroy com RxJS

```typescript
// Base component com destroy$ padrão
export abstract class BaseComponent implements OnInit, OnDestroy {
  protected destroy$ = new Subject<void>();

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected unsubscribeOnDestroy<T>(
    observable$: Observable<T>
  ): Observable<T> {
    return observable$.pipe(takeUntil(this.destroy$));
  }
}

// Uso em componentes
export class IncidentFormComponent extends BaseComponent {
  ngOnInit(): void {
    this.unsubscribeOnDestroy(this.myObservable$)
      .subscribe(value => {
        // Handle value
      });
  }
}
```

### 12.3 Operadores Comuns

| Operador | Uso | Exemplo |
|----------|-----|---------|
| `map()` | Transformar valores | `source$.pipe(map(x => x * 2))` |
| `switchMap()` | Flattening com cancel anterior | `clicks$.pipe(switchMap(() => http.get(...)))` |
| `mergeMap()` | Flattening sem cancel | `requests$.pipe(mergeMap(r => process(r)))` |
| `filter()` | Condicional | `values$.pipe(filter(v => v > 0))` |
| `takeUntil()` | Unsubscribe automático | `source$.pipe(takeUntil(destroy$))` |
| `debounceTime()` | Aguardar pausa | `search$.pipe(debounceTime(300))` |
| `distinctUntilChanged()` | Valores duplicados | `values$.pipe(distinctUntilChanged())` |
| `tap()` | Side effect (logging) | `requests$.pipe(tap(r => log(r)))` |
| `shareReplay()` | Cache compartilhado | `expensive$.pipe(shareReplay(1))` |

---

## 13. Estratégia de Componentização

### 13.1 Tipos de Componentes

| Tipo | Responsabilidade | OnPush | Example |
|------|-----------------|--------|---------|
| **Page** | Orquestra feature, gerencia estado | Não | `IncidentFormComponent` |
| **Smart** | Conecta ao store, lógica | Sim | `IncidentListComponent` |
| **Presentational** | Apenas exibe dados (Input/Output) | Sim | `IncidentBadgeComponent` |
| **Shared** | Reutilizável, agnóstico | Sim | `LoadingSpinnerComponent` |

### 13.2 Change Detection Strategy

**RFA-COMP-01:** Todos os componentes usam `ChangeDetectionStrategy.OnPush`

```typescript
@Component({
  selector: 'app-incident-badge',
  standalone: true,
  imports: [CommonModule],
  template: `<span class="badge" [class]="'badge-' + severity">{{ severity }}</span>`,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IncidentBadgeComponent {
  @Input() severity: string = '';
}
```

### 13.3 Comunicação entre Componentes

**RFA-COMP-02:** Preferir store (NgRx) para estado compartilhado

**RFA-COMP-03:** Usar Input/Output apenas para comunicação pai-filho

**RFA-COMP-04:** Services para dados que não são estado global

```typescript
// Exemplo: Parent → Child via Input
@Component({
  template: `<app-badge [severity]="incident.severity"></app-badge>`
})
export class IncidentCardComponent {
  @Input() incident!: IncidentRecord;
}

// Exemplo: Child → Parent via Output
@Component({
  template: `<button (click)="onDelete.emit(id)">Deletar</button>`
})
export class IncidentCardComponent {
  @Input() id!: string;
  @Output() onDelete = new EventEmitter<string>();
}
```

---

## 14. Estratégia de Estilização

### 14.1 Arquitetura CSS/SCSS

**RFA-STYLE-01:** SCSS com arquitetura ITCSS (Inverted Triangle CSS)

```
styles/
├── 00-settings/     # Variáveis, configurações
├── 01-tools/        # Mixins, funções
├── 02-generic/      # Reset, normalize
├── 03-elements/     # Tags HTML básicas
├── 04-objects/      # Classes agnósticas
├── 05-components/   # Componentes reutilizáveis
├── 06-utilities/    # Classes de utilidade
└── main.scss        # Import principal
```

### 14.2 Variáveis SCSS

```scss
// styles/00-settings/_variables.scss
$color-primary: #1976d2;
$color-success: #4caf50;
$color-warning: #ff9800;
$color-danger: #f44336;

$spacing-unit: 8px;
$spacing-xs: $spacing-unit;        // 8px
$spacing-sm: $spacing-unit * 2;    // 16px
$spacing-md: $spacing-unit * 3;    // 24px
$spacing-lg: $spacing-unit * 4;    // 32px

$border-radius: 4px;
$box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

$font-family-base: 'Roboto', sans-serif;
$font-size-base: 14px;
$line-height-base: 1.5;
```

### 14.3 Mixins Comuns

```scss
// styles/01-tools/_mixins.scss
@mixin flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

@mixin elevation($level: 1) {
  @if $level == 1 {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  } @else if $level == 2 {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }
}

@mixin responsive($breakpoint) {
  @if $breakpoint == 'mobile' {
    @media (max-width: 599px) { @content; }
  } @else if $breakpoint == 'tablet' {
    @media (min-width: 600px) and (max-width: 959px) { @content; }
  } @else if $breakpoint == 'desktop' {
    @media (min-width: 960px) { @content; }
  }
}
```

### 14.4 BEM Methodology

**RFA-STYLE-02:** Usar BEM para nomes de classes

```scss
// incident-badge.component.scss
.badge {
  @include flex-center;
  padding: $spacing-xs $spacing-sm;
  border-radius: $border-radius;
  font-weight: 500;

  &--success {
    background-color: $color-success;
    color: white;
  }

  &--warning {
    background-color: $color-warning;
    color: white;
  }

  &--danger {
    background-color: $color-danger;
    color: white;
  }
}
```

---

## 15. Angular Material Integration

### 15.1 Setup

```typescript
// app.config.ts
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';

export const appConfig: ApplicationConfig = {
  providers: [
    // ... outros providers
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule
  ]
};
```

### 15.2 Tema Material

```scss
// styles/theme.scss
@use '@angular/material' as mat;

$primary: mat.define-palette(mat.$blue-palette);
$accent: mat.define-palette(mat.$pink-palette, A200, A100, A400);
$warn: mat.define-palette(mat.$red-palette);

$theme: mat.define-light-theme((
  color: (
    primary: $primary,
    accent: $accent,
    warn: $warn,
  )
));

@include mat.all-component-colors($theme);
```

---

## 16. Responsividade

### 16.1 Breakpoints

**RFA-RESP-01:** Mobile-first approach

```scss
// Breakpoints
$breakpoint-mobile: 0px;
$breakpoint-tablet: 600px;
$breakpoint-desktop: 960px;
$breakpoint-large: 1280px;

// Mixins
@mixin mobile-only {
  @media (max-width: 599px) { @content; }
}

@mixin tablet-up {
  @media (min-width: 600px) { @content; }
}

@mixin desktop-up {
  @media (min-width: 960px) { @content; }
}
```

### 16.2 Layout Responsivo

```html
<!-- Template com grid responsivo -->
<div class="grid">
  <div class="grid__item grid__item--col-12 grid__item--tablet-col-6 grid__item--desktop-col-4">
    <!-- Conteúdo -->
  </div>
</div>
```

```scss
// CSS
.grid {
  display: grid;
  gap: $spacing-md;

  &__item {
    &--col-12 { grid-column: span 12; }

    @include tablet-up {
      &--tablet-col-6 { grid-column: span 6; }
    }

    @include desktop-up {
      &--desktop-col-4 { grid-column: span 4; }
    }
  }
}
```

---

## 17. Fluxo de Navegação

### 17.1 Routing Configuration

```typescript
// app.routes.ts
export const appRoutes: Routes = [
  {
    path: '',
    redirectTo: 'incident',
    pathMatch: 'full'
  },
  {
    path: 'incident',
    canActivate: [authGuard],
    loadChildren: () => 
      import('@modules/incident/incident.routes')
        .then(m => m.incidentRoutes),
    data: { title: 'Incidentes' }
  },
  {
    path: 'admin',
    canActivate: [authGuard, roleBasedGuard('ADMIN')],
    loadChildren: () => 
      import('@modules/admin/admin.routes')
        .then(m => m.adminRoutes),
    data: { title: 'Administração' }
  },
  {
    path: '**',
    redirectTo: 'incident'
  }
];
```

### 17.2 Feature Routes

```typescript
// modules/incident/incident.routes.ts
export const incidentRoutes: Routes = [
  {
    path: '',
    component: IncidentLayoutComponent,
    children: [
      {
        path: '',
        component: IncidentFormComponent,
        data: { title: 'Novo Incidente' }
      },
      {
        path: 'history',
        component: IncidentHistoryComponent,
        data: { title: 'Histórico de Incidentes' }
      },
      {
        path: ':id',
        component: IncidentDetailComponent,
        data: { title: 'Detalhes do Incidente' }
      }
    ]
  }
];
```

### 17.3 Navegação Programática

```typescript
constructor(private router: Router) {}

navigateToDetail(incidentId: string): void {
  this.router.navigate(['/incident', incidentId]);
}

navigateWithQuery(): void {
  this.router.navigate(['/incident/history'], {
    queryParams: { page: 1, sort: 'date' }
  });
}
```

---

## 18. Convenções de Nomes

### 18.1 Arquivos

| Tipo | Formato | Exemplo |
|------|---------|---------|
| Component | `kebab-case.component.ts` | `incident-form.component.ts` |
| Service | `kebab-case.service.ts` | `incident.service.ts` |
| Pipe | `kebab-case.pipe.ts` | `safe-html.pipe.ts` |
| Directive | `kebab-case.directive.ts` | `highlight.directive.ts` |
| Guard | `kebab-case.guard.ts` | `auth.guard.ts` |
| Interceptor | `kebab-case.interceptor.ts` | `auth.interceptor.ts` |
| Model | `kebab-case.model.ts` | `incident.model.ts` |
| DTO | `kebab-case.dto.ts` | `incident.dto.ts` |
| State | `kebab-case.state.ts` | `incident.state.ts` |
| Action | `kebab-case.actions.ts` | `incident.actions.ts` |

### 18.2 Classes e Interfaces

```typescript
// Classes (PascalCase)
export class IncidentFormComponent { }
export class IncidentService { }

// Interfaces (PascalCase, opcional: I prefix)
export interface IncidentRequest { }
export interface IIncidentRequest { }

// Enums (PascalCase)
export enum IncidentCategory { }

// Types (PascalCase ou camelCase)
export type IncidentCategory = 'SECURITY' | 'NETWORK' | ...;
```

### 18.3 Variáveis e Métodos

```typescript
// Variáveis (camelCase)
const currentUser = user;
let isLoading = false;

// Observables com $ suffix
const currentUser$ = this.userService.getCurrentUser();
const isLoading$ = this.store.select(selectLoading);

// Booleans com is/has prefix
isAuthenticated: boolean;
hasRole: boolean;
canDelete: boolean;

// Métodos (camelCase, verbo + substantivo)
getCurrentUser(): Observable<User> { }
classifyIncident(request: IncidentRequest): Observable<IncidentClassification> { }
validateForm(): boolean { }
onSubmit(): void { }
```

### 18.4 Seletores NgRx

```typescript
// Format: select[Feature][Property]
selectIncidents: MemoizedSelector<AppState, IncidentRecord[]>
selectLoading: MemoizedSelector<AppState, boolean>
selectIncidentError: MemoizedSelector<AppState, string | null>
```

---

## 19. Padrões de Código

### 19.1 Component Template Best Practices

```html
<!-- ✅ BOM: Usar async pipe para observables -->
<div *ngIf="isLoading$ | async">Loading...</div>

<!-- ✅ BOM: Usar trackBy em *ngFor -->
<div *ngFor="let incident of incidents$ | async; trackBy: trackByIncidentId">
  {{ incident.id }}
</div>

<!-- ✅ BOM: Usar safe navigation -->
<p>{{ currentUser$ | async | slice: 'name' }}</p>

<!-- ❌ RUIM: Subscriptions manuais no template -->
<div *ngIf="isLoadingManual">Loading...</div>
```

### 19.2 Service Best Practices

```typescript
// ✅ BOM: Tipagem completa
classifyIncident(request: IncidentRequest): Observable<IncidentClassification> {
  return this.http.post<IncidentClassification>(...);
}

// ✅ BOM: Tratamento de erro robusto
.pipe(
  catchError(error => {
    console.error('Error:', error);
    return throwError(() => new Error('Custom message'));
  })
)

// ❌ RUIM: any types
classifyIncident(request: any): Observable<any> {
  return this.http.post<any>(...);
}

// ❌ RUIM: Sem tratamento de erro
classifyIncident(request: IncidentRequest): Observable<IncidentClassification> {
  return this.http.post<IncidentClassification>(...);
}
```

### 19.3 Observable Best Practices

```typescript
// ✅ BOM: takeUntil para cleanup automático
private destroy$ = new Subject<void>();

ngOnInit(): void {
  this.observable$.pipe(
    takeUntil(this.destroy$)
  ).subscribe(value => {});
}

ngOnDestroy(): void {
  this.destroy$.next();
  this.destroy$.complete();
}

// ✅ BOM: shareReplay para evitar múltiplas subscriptions
readonly user$ = this.userService.getUser().pipe(shareReplay(1));

// ❌ RUIM: Manual subscription sem cleanup
ngOnInit(): void {
  this.observable$.subscribe(value => {});
}
```

---

## 20. Responsabilidades de Cada Camada

### 20.1 Arquitetura em Camadas

```
┌─────────────────────────────────────────┐
│          PRESENTATION LAYER              │
│  Components, Templates, Directives       │
│  - Exibir dados                         │
│  - Capturar inputs do usuário           │
│  - Renderizar UI                        │
└────────────────┬────────────────────────┘
                 │
┌─────────────────────────────────────────┐
│           APPLICATION LAYER              │
│  Containers, Smart Components            │
│  - Orquestra chamadas para dados        │
│  - Gerencia estado local                │
│  - Conecta ao Store (NgRx)              │
└────────────────┬────────────────────────┘
                 │
┌─────────────────────────────────────────┐
│           STATE MANAGEMENT LAYER         │
│  NgRx Store, Actions, Reducers          │
│  - Gerencia estado global               │
│  - Orquestra effects (side effects)     │
│  - Proporciona observables de estado    │
└────────────────┬────────────────────────┘
                 │
┌─────────────────────────────────────────┐
│            SERVICE LAYER                │
│  HttpClientService, IncidentService     │
│  - Chamadas HTTP                        │
│  - Transformação de dados               │
│  - Lógica de negócio reutilizável       │
└────────────────┬────────────────────────┘
                 │
┌─────────────────────────────────────────┐
│            API / BACKEND                │
│  REST endpoints                          │
│  - Processa requisições                 │
│  - Retorna dados                        │
└─────────────────────────────────────────┘
```

### 20.2 Responsabilidades Detalhadas

#### 20.2.1 Presentation Layer (Components)
- Renderizar UI via templates
- Capturar input do usuário
- Validação básica de formulários
- Formatação de dados para exibição
- **NÃO:** Chamadas HTTP, lógica de negócio complexa, gerenciamento de estado global

#### 20.2.2 Application Layer (Smart Components)
- Orquestra chamadas para dados
- Conecta componentes ao Store
- Gerencia navegação
- Dispara ações no Store
- Passa dados para componentes filhos
- **NÃO:** Chamadas HTTP diretas, renderização detalhada

#### 20.2.3 State Management Layer (NgRx)
- Gerencia estado global
- Define ações e reducers
- Orquestra effects (side effects)
- Proporciona observables tipados
- **NÃO:** Lógica de UI, renderização

#### 20.2.4 Service Layer
- Chamadas HTTP via HttpClientService
- Transformação de data transfer objects
- Lógica de negócio reutilizável
- Caching
- **NÃO:** Renderização, gerenciamento de estado direto

---

## 21. Checklist de Implementação

- [ ] Estrutura de pastas implementada
- [ ] AppComponent com layout base
- [ ] app.config.ts com providers
- [ ] app.routes.ts com lazy loading
- [ ] Core services (Auth, Incident, HttpClient, Notification)
- [ ] Guards (Auth, RoleBased, UnsavedChanges)
- [ ] Interceptors (Auth, Error, Loading)
- [ ] NgRx Store (Incident, UI)
- [ ] Shared components (Header, Sidebar, Footer, LoadingSpinner)
- [ ] Feature modules com routes
- [ ] Models e DTOs
- [ ] SCSS variables, mixins, theme
- [ ] Angular Material integrado
- [ ] Responsividade implementada
- [ ] RxJS patterns (takeUntil, shareReplay, etc.)
- [ ] Type safety (strict mode)
- [ ] Testes unitários para services
- [ ] Testes de componentes

---

## 22. Referências

- [Angular 17 Documentation](https://angular.io)
- [NgRx Documentation](https://ngrx.io)
- [RxJS Documentation](https://rxjs.dev)
- [Angular Material](https://material.angular.io)
- [Angular Style Guide](https://angular.io/guide/styleguide)
- [Clean Code Principles](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)

---

**Versão:** 1.0  
**Última Atualização:** Maio 2026  
**Status:** Ativo
