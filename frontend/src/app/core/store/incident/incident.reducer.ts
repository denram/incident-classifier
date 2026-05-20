import { createReducer, on } from '@ngrx/store';
import { initialIncidentState } from './incident.state';
import { incidentActions, incidentApiActions, incidentFormActions, incidentHistoryActions } from './incident.actions';

export const incidentReducer = createReducer(
  initialIncidentState,

  on(incidentFormActions.classifyIncident, state => ({
    ...state, loading: true, error: null
  })),
  on(incidentApiActions.classifyIncidentSuccess, (state, { classification }) => ({
    ...state, classification, loading: false
  })),
  on(incidentApiActions.classifyIncidentError, (state, { error }) => ({
    ...state, error, loading: false
  })),

  on(incidentHistoryActions.loadIncidents, state => ({
    ...state, loading: true, error: null
  })),
  on(incidentApiActions.loadIncidentsSuccess, (state, { incidents }) => ({
    ...state, incidents, loading: false
  })),
  on(incidentApiActions.loadIncidentsError, (state, { error }) => ({
    ...state, error, loading: false
  })),

  on(incidentActions.clearClassification, state => ({
    ...state, classification: null
  })),
  on(incidentActions.clearError, state => ({
    ...state, error: null
  }))
);
