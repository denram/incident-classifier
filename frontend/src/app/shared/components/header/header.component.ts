import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-header',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterLink, RouterLinkActive, MatToolbarModule, MatButtonModule, MatIconModule],
  template: `
    <mat-toolbar color="primary" class="app-toolbar">
      <div class="toolbar-brand">
        <mat-icon>security</mat-icon>
        <span class="brand-name">SICO</span>
        <span class="brand-subtitle">Sistema de Classificação de Ocorrências</span>
      </div>
      <nav class="toolbar-nav">
        <a mat-button routerLink="/incident/new" routerLinkActive="nav-active">
          <mat-icon>add_circle_outline</mat-icon>
          Novo Incidente
        </a>
        <a mat-button routerLink="/incident/history" routerLinkActive="nav-active">
          <mat-icon>history</mat-icon>
          Histórico
        </a>
      </nav>
    </mat-toolbar>
  `,
  styles: [`
    .app-toolbar {
      position: sticky;
      top: 0;
      z-index: 100;
      gap: 16px;
    }
    .toolbar-brand {
      display: flex;
      align-items: center;
      gap: 8px;
      flex: 1;
    }
    .brand-name {
      font-size: 20px;
      font-weight: 700;
      letter-spacing: 1px;
    }
    .brand-subtitle {
      font-size: 12px;
      opacity: 0.8;
      display: none;
    }
    @media (min-width: 768px) {
      .brand-subtitle { display: inline; }
    }
    .toolbar-nav {
      display: flex;
      gap: 4px;
    }
    .nav-active {
      background: rgba(255,255,255,0.15);
      border-radius: 4px;
    }
  `]
})
export class HeaderComponent {}
