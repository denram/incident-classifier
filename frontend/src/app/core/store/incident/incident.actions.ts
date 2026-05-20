import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { IncidentClassification, IncidentRecord, IncidentRequest } from '../../models/incident.model';

export const incidentFormActions = createActionGroup({
  source: 'Incident Form',
  events: {
    'Classify Incident': props<{ request: IncidentRequest }>()
  }
});

export const incidentHistoryActions = createActionGroup({
  source: 'Incident History',
  events: {
    'Load Incidents': emptyProps()
  }
});

export const incidentApiActions = createActionGroup({
  source: 'Incident API',
  events: {
    'Classify Incident Success': props<{ classification: IncidentClassification }>(),
    'Classify Incident Error':   props<{ error: string }>(),
    'Load Incidents Success':    props<{ incidents: IncidentRecord[] }>(),
    'Load Incidents Error':      props<{ error: string }>()
  }
});

export const incidentActions = createActionGroup({
  source: 'Incident',
  events: {
    'Clear Classification': emptyProps(),
    'Clear Error':          emptyProps()
  }
});
