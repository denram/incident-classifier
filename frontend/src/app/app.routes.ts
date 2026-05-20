import { Routes } from '@angular/router';

export const APP_ROUTES: Routes = [
  {
    path: 'incident',
    loadChildren: () =>
      import('./modules/incident/incident.routes').then(m => m.INCIDENT_ROUTES)
  },
  { path: '', redirectTo: 'incident/new', pathMatch: 'full' },
  { path: '**', redirectTo: 'incident/new' }
];
