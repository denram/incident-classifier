import { Routes } from '@angular/router';

export const INCIDENT_ROUTES: Routes = [
  {
    path: 'new',
    loadComponent: () =>
      import('./pages/incident-form/incident-form.component').then(m => m.IncidentFormComponent),
    title: 'Registrar Incidente — SICO'
  },
  {
    path: 'history',
    loadComponent: () =>
      import('./pages/incident-history/incident-history.component').then(m => m.IncidentHistoryComponent),
    title: 'Histórico — SICO'
  },
  { path: '', redirectTo: 'new', pathMatch: 'full' }
];
