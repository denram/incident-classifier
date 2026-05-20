import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-error-alert',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <mat-card class="error-alert" *ngIf="show && message">
      <mat-card-content>
        <mat-icon color="warn">error_outline</mat-icon>
        <span>{{ message }}</span>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .error-alert {
      background-color: #fdecea;
      border-left: 4px solid #f44336;
      margin-bottom: 16px;
    }
    mat-card-content {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px !important;
      margin: 0 !important;
    }
    span { font-size: 14px; }
  `]
})
export class ErrorAlertComponent {
  @Input() message = '';
  @Input() show    = false;
}
