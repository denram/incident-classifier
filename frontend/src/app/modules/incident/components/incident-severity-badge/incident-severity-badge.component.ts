import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatChipsModule } from '@angular/material/chips';
import { IncidentSeverity, SEVERITY_LABELS } from '@core/models/incident.model';

const SEVERITY_STYLES: Record<IncidentSeverity, { bg: string; color: string }> = {
  [IncidentSeverity.LOW]:    { bg: '#e8f5e9', color: '#2e7d32' },
  [IncidentSeverity.MEDIUM]: { bg: '#fff8e1', color: '#f57f17' },
  [IncidentSeverity.HIGH]:   { bg: '#ffebee', color: '#c62828' }
};

@Component({
  selector: 'app-incident-severity-badge',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatChipsModule],
  template: `
    <mat-chip class="severity-chip" [disableRipple]="true" [style.background-color]="style.bg" [style.color]="style.color">
      {{ label }}
    </mat-chip>
  `,
  styles: [`
    .severity-chip {
      font-size: 13px;
      font-weight: 600;
      pointer-events: none;
    }
  `]
})
export class IncidentSeverityBadgeComponent {
  @Input() severity!: IncidentSeverity;

  get label(): string { return SEVERITY_LABELS[this.severity] ?? this.severity; }
  get style() { return SEVERITY_STYLES[this.severity] ?? { bg: '#f5f5f5', color: '#616161' }; }
}
