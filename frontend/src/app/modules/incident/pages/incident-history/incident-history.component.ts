import { ChangeDetectionStrategy, Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';

import { incidentHistoryActions } from '@core/store/incident/incident.actions';
import { selectIncidents, selectIncidentLoading, selectIncidentError } from '@core/store/incident/incident.selectors';
import { IncidentRecord } from '@core/models/incident.model';
import { LoadingSpinnerComponent } from '@shared/components/loading-spinner/loading-spinner.component';
import { ErrorAlertComponent } from '@shared/components/error-alert/error-alert.component';
import { IncidentCategoryBadgeComponent } from '../../components/incident-category-badge/incident-category-badge.component';
import { IncidentSeverityBadgeComponent } from '../../components/incident-severity-badge/incident-severity-badge.component';
import { FormatDatePipe } from '@shared/pipes/format-date.pipe';

@Component({
  selector: 'app-incident-history',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    MatTooltipModule,
    LoadingSpinnerComponent,
    ErrorAlertComponent,
    IncidentCategoryBadgeComponent,
    IncidentSeverityBadgeComponent,
    FormatDatePipe
  ],
  templateUrl: './incident-history.component.html',
  styleUrls: ['./incident-history.component.scss']
})
export class IncidentHistoryComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;

  readonly loading$ = this.store.select(selectIncidentLoading);
  readonly error$   = this.store.select(selectIncidentError);

  displayedColumns = ['id', 'registeredAt', 'category', 'severity', 'originalText', 'suggestedAction'];
  dataSource = new MatTableDataSource<IncidentRecord>([]);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(incidentHistoryActions.loadIncidents());
    this.store.select(selectIncidents).subscribe(incidents => {
      this.dataSource.data = incidents;
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  reload(): void {
    this.store.dispatch(incidentHistoryActions.loadIncidents());
  }
}
