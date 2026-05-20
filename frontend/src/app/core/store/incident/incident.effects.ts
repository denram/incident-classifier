import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of, timer } from 'rxjs';
import { catchError, map, retry, switchMap, tap } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { IncidentService } from '../../services/incident.service';
import { NotificationService } from '../../services/notification.service';
import { incidentApiActions, incidentFormActions, incidentHistoryActions } from './incident.actions';

@Injectable()
export class IncidentEffects {

  classifyIncident$ = createEffect(() =>
    this.actions$.pipe(
      ofType(incidentFormActions.classifyIncident),
      switchMap(({ request }) =>
        this.incidentService.classifyIncident(request).pipe(
          retry({
            count: 3,
            delay: (err: HttpErrorResponse, count) =>
              err.status === 0 ? timer(count * 5000) : of(null).pipe(map(() => { throw err; }))
          }),
          map(classification => incidentApiActions.classifyIncidentSuccess({ classification })),
          tap(() => this.notification.success('Incidente classificado com sucesso!')),
          catchError((err: HttpErrorResponse) =>
            of(incidentApiActions.classifyIncidentError({
              error: err.error?.message ?? 'Erro ao classificar incidente.'
            }))
          )
        )
      )
    )
  );

  loadIncidents$ = createEffect(() =>
    this.actions$.pipe(
      ofType(incidentHistoryActions.loadIncidents),
      switchMap(() =>
        this.incidentService.listIncidents().pipe(
          map(incidents => incidentApiActions.loadIncidentsSuccess({ incidents })),
          catchError((err: HttpErrorResponse) =>
            of(incidentApiActions.loadIncidentsError({
              error: err.error?.message ?? 'Erro ao carregar histórico.'
            }))
          )
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private incidentService: IncidentService,
    private notification: NotificationService
  ) {}
}
