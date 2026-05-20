export enum IncidentCategory {
  SECURITY       = 'SECURITY',
  NETWORK        = 'NETWORK',
  HARDWARE       = 'HARDWARE',
  SOFTWARE       = 'SOFTWARE',
  DATABASE       = 'DATABASE',
  DATA_LOSS      = 'DATA_LOSS',
  ACCESS_CONTROL = 'ACCESS_CONTROL',
  PERFORMANCE    = 'PERFORMANCE',
  SERVICE_OUTAGE = 'SERVICE_OUTAGE',
  INFRASTRUCTURE = 'INFRASTRUCTURE',
  COMMUNICATION  = 'COMMUNICATION',
  COMPLIANCE     = 'COMPLIANCE',
  OTHER          = 'OTHER'
}

export enum IncidentSeverity {
  LOW    = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH   = 'HIGH'
}

export interface IncidentRequest {
  incident: string;
}

export interface IncidentClassification {
  formalizedIncidentText: string;
  category: IncidentCategory;
  severity: IncidentSeverity;
  registeredAt: string;
  suggestedAction: string;
}

export interface IncidentRecord extends IncidentClassification {
  id: number;
  originalText: string;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  timestamp: string;
}

export const CATEGORY_LABELS: Record<IncidentCategory, string> = {
  [IncidentCategory.SECURITY]:       'Segurança',
  [IncidentCategory.NETWORK]:        'Rede',
  [IncidentCategory.HARDWARE]:       'Hardware',
  [IncidentCategory.SOFTWARE]:       'Software',
  [IncidentCategory.DATABASE]:       'Banco de Dados',
  [IncidentCategory.DATA_LOSS]:      'Perda de Dados',
  [IncidentCategory.ACCESS_CONTROL]: 'Controle de Acesso',
  [IncidentCategory.PERFORMANCE]:    'Performance',
  [IncidentCategory.SERVICE_OUTAGE]: 'Serviço Indisponível',
  [IncidentCategory.INFRASTRUCTURE]: 'Infraestrutura',
  [IncidentCategory.COMMUNICATION]:  'Comunicação',
  [IncidentCategory.COMPLIANCE]:     'Conformidade',
  [IncidentCategory.OTHER]:          'Outro'
};

export const SEVERITY_LABELS: Record<IncidentSeverity, string> = {
  [IncidentSeverity.LOW]:    'Baixa',
  [IncidentSeverity.MEDIUM]: 'Média',
  [IncidentSeverity.HIGH]:   'Alta'
};
