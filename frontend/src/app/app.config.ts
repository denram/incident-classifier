import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';

import { APP_ROUTES } from './app.routes';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { loadingInterceptor } from './core/interceptors/loading.interceptor';
import { incidentReducer } from './core/store/incident/incident.reducer';
import { uiReducer } from './core/store/ui/ui.reducer';
import { IncidentEffects } from './core/store/incident/incident.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(APP_ROUTES, withComponentInputBinding()),
    provideHttpClient(withInterceptors([errorInterceptor, loadingInterceptor])),
    provideAnimations(),
    provideStore({
      incident: incidentReducer,
      ui: uiReducer
    }),
    provideEffects([IncidentEffects])
  ]
};
