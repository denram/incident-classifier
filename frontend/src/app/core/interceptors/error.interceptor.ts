import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';
import { ApiError } from '../models/incident.model';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notification = inject(NotificationService);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      const message = resolveMessage(err);
      notification.error(message);
      return throwError(() => err);
    })
  );
};

function resolveMessage(err: HttpErrorResponse): string {
  if (err.status === 400) {
    const body = err.error as ApiError | null;
    return body?.message ?? 'Dados inválidos. Verifique o formulário.';
  }
  if (err.status === 422) {
    return 'Falha ao classificar o incidente. Tente novamente.';
  }
  if (err.status === 500) {
    return 'Erro no servidor. Tente novamente.';
  }
  if (err.status === 0) {
    return 'Erro de conexão. Verifique sua internet.';
  }
  return `Erro inesperado (${err.status}). Tente novamente.`;
}
