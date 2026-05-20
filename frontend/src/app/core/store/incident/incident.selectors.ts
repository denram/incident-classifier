import { createFeatureSelector, createSelector } from '@ngrx/store';
import { IncidentState } from './incident.state';

const selectIncidentState = createFeatureSelector<IncidentState>('incident');

export const selectIncidents      = createSelector(selectIncidentState, s => s.incidents);
export const selectClassification = createSelector(selectIncidentState, s => s.classification);
export const selectIncidentLoading = createSelector(selectIncidentState, s => s.loading);
export const selectIncidentError  = createSelector(selectIncidentState, s => s.error);
