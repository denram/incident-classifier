import { IncidentClassification, IncidentRecord } from '../../models/incident.model';

export interface IncidentState {
  incidents: IncidentRecord[];
  classification: IncidentClassification | null;
  loading: boolean;
  error: string | null;
}

export const initialIncidentState: IncidentState = {
  incidents: [],
  classification: null,
  loading: false,
  error: null
};
