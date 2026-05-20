import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientService } from './http-client.service';
import { IncidentClassification, IncidentRecord, IncidentRequest } from '../models/incident.model';

@Injectable({ providedIn: 'root' })
export class IncidentService {
  private readonly endpoint = '/incidentClassifier';

  constructor(private http: HttpClientService) {}

  classifyIncident(request: IncidentRequest): Observable<IncidentClassification> {
    return this.http.post<IncidentClassification>(this.endpoint, request);
  }

  listIncidents(): Observable<IncidentRecord[]> {
    return this.http.get<IncidentRecord[]>(this.endpoint);
  }
}
