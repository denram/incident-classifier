import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, timeout } from 'rxjs/operators';
import { environment } from '@environments/environment';

@Injectable({ providedIn: 'root' })
export class HttpClientService {
  private readonly baseUrl = environment.apiUrl;
  private readonly postTimeout = 30_000;
  private readonly getTimeout  = 5_000;

  constructor(private http: HttpClient) {}

  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}${endpoint}`).pipe(
      timeout(this.getTimeout),
      catchError(this.rethrow)
    );
  }

  post<T>(endpoint: string, body: unknown): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${endpoint}`, body).pipe(
      timeout(this.postTimeout),
      catchError(this.rethrow)
    );
  }

  private rethrow(err: HttpErrorResponse): Observable<never> {
    return throwError(() => err);
  }
}
