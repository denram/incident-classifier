import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Subject, takeUntil } from 'rxjs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';

import { incidentFormActions, incidentActions } from '@core/store/incident/incident.actions';
import {
  selectClassification,
  selectIncidentLoading,
  selectIncidentError
} from '@core/store/incident/incident.selectors';
import { LoadingSpinnerComponent } from '@shared/components/loading-spinner/loading-spinner.component';
import { ErrorAlertComponent } from '@shared/components/error-alert/error-alert.component';
import { IncidentCategoryBadgeComponent } from '../../components/incident-category-badge/incident-category-badge.component';
import { IncidentSeverityBadgeComponent } from '../../components/incident-severity-badge/incident-severity-badge.component';
import { FormatDatePipe } from '@shared/pipes/format-date.pipe';

@Component({
  selector: 'app-incident-form',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    LoadingSpinnerComponent,
    ErrorAlertComponent,
    IncidentCategoryBadgeComponent,
    IncidentSeverityBadgeComponent,
    FormatDatePipe
  ],
  templateUrl: './incident-form.component.html',
  styleUrls: ['./incident-form.component.scss']
})
export class IncidentFormComponent implements OnInit, OnDestroy {
  readonly loading$        = this.store.select(selectIncidentLoading);
  readonly error$          = this.store.select(selectIncidentError);
  readonly classification$ = this.store.select(selectClassification);

  form!: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private store: Store) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      incident: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(5000)]]
    });

    this.store.select(selectClassification).pipe(takeUntil(this.destroy$)).subscribe(result => {
      if (result) this.form.reset();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.store.dispatch(incidentActions.clearClassification());
    this.store.dispatch(incidentActions.clearError());
  }

  get incidentControl() { return this.form.get('incident')!; }

  get charCount(): number { return this.incidentControl.value?.length ?? 0; }

  submit(): void {
    if (this.form.invalid) return;
    this.store.dispatch(incidentActions.clearClassification());
    this.store.dispatch(incidentActions.clearError());
    this.store.dispatch(incidentFormActions.classifyIncident({ request: { incident: this.incidentControl.value } }));
  }

  clearResult(): void {
    this.store.dispatch(incidentActions.clearClassification());
    this.store.dispatch(incidentActions.clearError());
  }

  getErrorMessage(): string {
    const ctrl = this.incidentControl;
    if (ctrl.hasError('required'))   return 'Descrição do incidente é obrigatória.';
    if (ctrl.hasError('minlength'))  return 'Descrição deve ter no mínimo 10 caracteres.';
    if (ctrl.hasError('maxlength'))  return 'Descrição deve ter no máximo 5000 caracteres.';
    return '';
  }
}
