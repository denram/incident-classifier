import { incidentReducer } from './incident.reducer';
import { initialIncidentState } from './incident.state';
import { incidentActions, incidentApiActions, incidentFormActions, incidentHistoryActions } from './incident.actions';
import { IncidentCategory, IncidentSeverity } from '../../models/incident.model';

const mockClassification = {
  formalizedIncidentText: 'Falha de segurança detectada.',
  category: IncidentCategory.SECURITY,
  severity: IncidentSeverity.HIGH,
  registeredAt: '2024-01-01T00:00:00',
  suggestedAction: 'Isolar o sistema afetado.'
};

const mockIncident = { id: 1, originalText: 'Servidor caiu', ...mockClassification };

describe('incidentReducer', () => {
  it('returns the initial state for an unknown action', () => {
    const state = incidentReducer(undefined, { type: '@@INIT' } as any);
    expect(state).toEqual(initialIncidentState);
  });

  describe('classifyIncident', () => {
    it('sets loading true and clears error', () => {
      const state = incidentReducer(
        { ...initialIncidentState, error: 'previous error' },
        incidentFormActions.classifyIncident({ request: { incident: 'test' } })
      );
      expect(state.loading).toBe(true);
      expect(state.error).toBeNull();
    });

    it('stores classification and sets loading false on success', () => {
      const state = incidentReducer(
        { ...initialIncidentState, loading: true },
        incidentApiActions.classifyIncidentSuccess({ classification: mockClassification })
      );
      expect(state.loading).toBe(false);
      expect(state.classification).toEqual(mockClassification);
    });

    it('stores error and sets loading false on failure', () => {
      const state = incidentReducer(
        { ...initialIncidentState, loading: true },
        incidentApiActions.classifyIncidentError({ error: 'Erro ao classificar.' })
      );
      expect(state.loading).toBe(false);
      expect(state.error).toBe('Erro ao classificar.');
    });
  });

  describe('loadIncidents', () => {
    it('sets loading true and clears error', () => {
      const state = incidentReducer(
        initialIncidentState,
        incidentHistoryActions.loadIncidents()
      );
      expect(state.loading).toBe(true);
      expect(state.error).toBeNull();
    });

    it('stores incidents and sets loading false on success', () => {
      const state = incidentReducer(
        { ...initialIncidentState, loading: true },
        incidentApiActions.loadIncidentsSuccess({ incidents: [mockIncident] })
      );
      expect(state.loading).toBe(false);
      expect(state.incidents).toEqual([mockIncident]);
    });

    it('stores error and sets loading false on failure', () => {
      const state = incidentReducer(
        { ...initialIncidentState, loading: true },
        incidentApiActions.loadIncidentsError({ error: 'Erro ao carregar.' })
      );
      expect(state.loading).toBe(false);
      expect(state.error).toBe('Erro ao carregar.');
    });
  });

  describe('clear actions', () => {
    it('clears classification', () => {
      const state = incidentReducer(
        { ...initialIncidentState, classification: mockClassification },
        incidentActions.clearClassification()
      );
      expect(state.classification).toBeNull();
    });

    it('clears error', () => {
      const state = incidentReducer(
        { ...initialIncidentState, error: 'some error' },
        incidentActions.clearError()
      );
      expect(state.error).toBeNull();
    });
  });
});
