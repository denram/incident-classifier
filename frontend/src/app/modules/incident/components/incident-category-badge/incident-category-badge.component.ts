import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { IncidentCategory, CATEGORY_LABELS } from '@core/models/incident.model';

const CATEGORY_ICONS: Record<IncidentCategory, string> = {
  [IncidentCategory.SECURITY]:       'shield',
  [IncidentCategory.NETWORK]:        'wifi',
  [IncidentCategory.HARDWARE]:       'memory',
  [IncidentCategory.SOFTWARE]:       'bug_report',
  [IncidentCategory.DATABASE]:       'storage',
  [IncidentCategory.DATA_LOSS]:      'cloud_off',
  [IncidentCategory.ACCESS_CONTROL]: 'lock',
  [IncidentCategory.PERFORMANCE]:    'speed',
  [IncidentCategory.SERVICE_OUTAGE]: 'power_off',
  [IncidentCategory.INFRASTRUCTURE]: 'dns',
  [IncidentCategory.COMMUNICATION]:  'chat_bubble_outline',
  [IncidentCategory.COMPLIANCE]:     'gavel',
  [IncidentCategory.OTHER]:          'help_outline'
};

@Component({
  selector: 'app-incident-category-badge',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatChipsModule, MatIconModule],
  template: `
    <mat-chip class="category-chip" [disableRipple]="true">
      <mat-icon matChipAvatar>{{ icon }}</mat-icon>
      {{ label }}
    </mat-chip>
  `,
  styles: [`
    .category-chip {
      background-color: #e3f2fd;
      color: #1565c0;
      font-size: 13px;
      pointer-events: none;
    }
  `]
})
export class IncidentCategoryBadgeComponent {
  @Input() category!: IncidentCategory;

  get label(): string { return CATEGORY_LABELS[this.category] ?? this.category; }
  get icon(): string  { return CATEGORY_ICONS[this.category]  ?? 'label'; }
}
