# Diretrizes de Design UI/UX — SICO
## Sistema Inteligente de Registro e Classificação de Ocorrências Operacionais

**Versão:** 1.0  
**Data:** Maio de 2026  
**Status:** Aprovado  
**Classificação:** Interno  
**Autor:** Equipe de Design e Arquitetura Frontend  
**Stack:** Angular 17 + Angular Material 17 + SCSS

---

## Sumário

1. [Identidade Visual](#1-identidade-visual)
2. [Tipografia](#2-tipografia)
3. [Espaçamento e Ritmo Visual](#3-espaçamento-e-ritmo-visual)
4. [Grid System e Layout](#4-grid-system-e-layout)
5. [Componentes Visuais](#5-componentes-visuais)
6. [Formulários](#6-formulários)
7. [Botões](#7-botões)
8. [Tabelas e Listas](#8-tabelas-e-listas)
9. [Feedback Visual](#9-feedback-visual)
10. [Responsividade](#10-responsividade)
11. [Acessibilidade](#11-acessibilidade)
12. [Dark Mode](#12-dark-mode)
13. [Boas Práticas UX](#13-boas-práticas-ux)
14. [Padrões Angular Material](#14-padrões-angular-material)

---

## 1. Identidade Visual

### 1.1 Princípio Geral

O SICO é uma ferramenta de operação crítica. A identidade visual deve transmitir **confiabilidade, clareza e precisão**. Não há espaço para elementos decorativos sem função. Cada elemento visual carrega significado operacional.

> **Regra fundamental:** A interface deve parecer um cockpit, não um tutorial. Operadores técnicos precisam de densidade de informação com clareza hierárquica — não de interfaces simplificadas.

### 1.2 Paleta de Cores

#### Cores Primárias

| Token | Hex | Uso |
|-------|-----|-----|
| `--color-primary-900` | `#0d47a1` | Texto sobre fundo claro, links |
| `--color-primary-700` | `#1565c0` | Hover de elementos primários |
| `--color-primary-600` | `#1976d2` | **CTA primário, headers, foco** |
| `--color-primary-400` | `#42a5f5` | Estados ativos, indicadores |
| `--color-primary-100` | `#bbdefb` | Backgrounds sutis, seleção |
| `--color-primary-50`  | `#e3f2fd` | Backgrounds muito sutis |

#### Cores Neutras (Grays)

| Token | Hex | Uso |
|-------|-----|-----|
| `--color-neutral-900` | `#212121` | Texto principal |
| `--color-neutral-700` | `#424242` | Texto secundário |
| `--color-neutral-500` | `#9e9e9e` | Placeholder, texto desabilitado |
| `--color-neutral-300` | `#e0e0e0` | Bordas, divisores |
| `--color-neutral-100` | `#f5f5f5` | Backgrounds de tabela (zebra) |
| `--color-neutral-50`  | `#fafafa` | Background geral da aplicação |
| `--color-white`       | `#ffffff` | Cards, modais, superfícies |

#### Cores Semânticas de Severidade

Derivadas das regras de negócio (RN-7.3.1). Cada nível tem variantes para fundos e textos acessíveis.

| Severidade | Cor Principal | Fundo (chip) | Texto no Fundo |
|------------|--------------|--------------|----------------|
| `LOW` / Baixa | `#2e7d32` | `#e8f5e9` | `#1b5e20` |
| `MEDIUM` / Média | `#f57f17` | `#fff8e1` | `#e65100` |
| `HIGH` / Alta | `#e64a19` | `#fbe9e7` | `#bf360c` |
| `CRITICAL` / Crítica | `#b71c1c` | `#ffebee` | `#7f0000` |

```scss
// styles/00-settings/_severity-colors.scss
:root {
  // LOW
  --severity-low-main:   #2e7d32;
  --severity-low-bg:     #e8f5e9;
  --severity-low-text:   #1b5e20;

  // MEDIUM
  --severity-medium-main: #f57f17;
  --severity-medium-bg:   #fff8e1;
  --severity-medium-text: #e65100;

  // HIGH
  --severity-high-main:  #e64a19;
  --severity-high-bg:    #fbe9e7;
  --severity-high-text:  #bf360c;

  // CRITICAL
  --severity-critical-main: #b71c1c;
  --severity-critical-bg:   #ffebee;
  --severity-critical-text: #7f0000;
}
```

#### Cores Semânticas de Categoria

Mapeamento visual diferenciado para as 13 categorias do sistema (RF-16):

| Categoria | Cor Principal | Fundo Chip | Ícone Material |
|-----------|--------------|------------|----------------|
| `SECURITY` | `#b71c1c` | `#ffebee` | `security` |
| `NETWORK` | `#e65100` | `#fbe9e7` | `wifi_tethering` |
| `HARDWARE` | `#4e342e` | `#efebe9` | `memory` |
| `SOFTWARE` | `#4527a0` | `#ede7f6` | `code` |
| `DATABASE` | `#1565c0` | `#e3f2fd` | `storage` |
| `DATA_LOSS` | `#880e4f` | `#fce4ec` | `folder_off` |
| `ACCESS_CONTROL` | `#f57f17` | `#fff8e1` | `lock` |
| `PERFORMANCE` | `#827717` | `#f9fbe7` | `speed` |
| `SERVICE_OUTAGE` | `#c62828` | `#ffebee` | `cloud_off` |
| `INFRASTRUCTURE` | `#37474f` | `#eceff1` | `dns` |
| `COMMUNICATION` | `#00695c` | `#e0f2f1` | `hub` |
| `COMPLIANCE` | `#4a148c` | `#f3e5f5` | `gavel` |
| `OTHER` | `#546e7a` | `#eceff1` | `category` |

```scss
// styles/00-settings/_category-colors.scss
$categories: (
  'SECURITY':       (#b71c1c, #ffebee),
  'NETWORK':        (#e65100, #fbe9e7),
  'HARDWARE':       (#4e342e, #efebe9),
  'SOFTWARE':       (#4527a0, #ede7f6),
  'DATABASE':       (#1565c0, #e3f2fd),
  'DATA_LOSS':      (#880e4f, #fce4ec),
  'ACCESS_CONTROL': (#f57f17, #fff8e1),
  'PERFORMANCE':    (#827717, #f9fbe7),
  'SERVICE_OUTAGE': (#c62828, #ffebee),
  'INFRASTRUCTURE': (#37474f, #eceff1),
  'COMMUNICATION':  (#00695c, #e0f2f1),
  'COMPLIANCE':     (#4a148c, #f3e5f5),
  'OTHER':          (#546e7a, #eceff1),
);
```

### 1.3 Logotipo e Marca

- **Nome exibido:** SICO — Classificador de Ocorrências
- **Símbolo:** Ícone `monitoring` do Material Icons (ou equivalente) combinado com nome
- **Cor do logo:** `--color-primary-600` (#1976d2) sobre fundo branco; branco sobre primary no header
- **Área de proteção:** Mínimo de 16px de espaço livre ao redor do logo
- **Uso incorreto:** Não distorcer proporções, não usar sobre fundos que comprometam contraste

### 1.4 Iconografia

- **Biblioteca:** Angular Material Icons (`mat-icon`) — sem ícones de terceiros no MVP
- **Tamanhos:**
  - 16px — contexto inline (tabela, label)
  - 20px — botões e chips
  - 24px — padrão componente
  - 32px — destaques e empty states
  - 48px — empty states principais
- **Regra:** Ícone sempre acompanhado de texto ou `aria-label`. Nunca ícone isolado sem semântica.

---

## 2. Tipografia

### 2.1 Família Tipográfica

```scss
// Fonte principal — Material Design padrão
$font-family-base:  'Roboto', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;

// Monospace — para texto formalizado pela IA, timestamps, IDs
$font-family-mono:  'Roboto Mono', 'Consolas', 'Courier New', monospace;
```

**Carregamento (index.html):**
```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&family=Roboto+Mono:wght@400;500&display=swap" rel="stylesheet">
```

### 2.2 Escala Tipográfica

Baseada na escala do Material Design 3, adaptada para contexto corporativo enterprise:

| Token | Tamanho | Peso | Line-height | Uso |
|-------|---------|------|-------------|-----|
| `headline-1` | 32px | 300 | 1.2 | Títulos de página (H1) |
| `headline-2` | 24px | 400 | 1.3 | Subtítulos de seção (H2) |
| `headline-3` | 20px | 500 | 1.4 | Títulos de card (H3) |
| `title-large` | 18px | 500 | 1.4 | Labels de formulário, thead |
| `title-medium` | 16px | 500 | 1.5 | Texto de ação, rótulos |
| `body-large` | 16px | 400 | 1.5 | **Corpo de texto principal** |
| `body-medium` | 14px | 400 | 1.5 | Texto padrão, conteúdo |
| `body-small` | 12px | 400 | 1.4 | Metadados, timestamps |
| `label-large` | 14px | 500 | 1.4 | Botões, chips, badges |
| `label-medium` | 12px | 500 | 1.3 | Rótulos inline, caption |
| `mono-body` | 14px | 400 | 1.6 | Texto formalizado pela IA |

```scss
// styles/01-typography/_scale.scss
%text-headline-1 { font-size: 32px; font-weight: 300; line-height: 1.2; letter-spacing: -0.5px; }
%text-headline-2 { font-size: 24px; font-weight: 400; line-height: 1.3; }
%text-headline-3 { font-size: 20px; font-weight: 500; line-height: 1.4; }
%text-title-large  { font-size: 18px; font-weight: 500; line-height: 1.4; }
%text-title-medium { font-size: 16px; font-weight: 500; line-height: 1.5; }
%text-body-large   { font-size: 16px; font-weight: 400; line-height: 1.5; }
%text-body-medium  { font-size: 14px; font-weight: 400; line-height: 1.5; }
%text-body-small   { font-size: 12px; font-weight: 400; line-height: 1.4; color: var(--color-neutral-700); }
%text-label-large  { font-size: 14px; font-weight: 500; line-height: 1.4; letter-spacing: 0.1px; }
%text-mono         { font-family: $font-family-mono; font-size: 14px; line-height: 1.6; }
```

### 2.3 Regras de Uso

- **Texto corrido:** `body-medium` (14px/400). Base para todos os conteúdos de tabela e formulário
- **Texto formalizado pela IA:** `mono-body` com `white-space: pre-wrap` para preservar quebras
- **Timestamps e IDs:** `body-small` + `color: neutral-700`
- **Botões:** `label-large` (14px/500) — nunca uppercase forçado
- **Cabeçalhos de tabela:** `label-medium` (12px/500) + uppercase + `letter-spacing: 0.08em`
- **Mínimo legível:** Nunca abaixo de 12px para qualquer texto funcional

---

## 3. Espaçamento e Ritmo Visual

### 3.1 Sistema de 8pt Grid

Todos os espaçamentos derivam de múltiplos de **8px**. Para casos excepcionais, múltiplos de **4px**.

```scss
// styles/00-settings/_spacing.scss
$spacing-unit: 8px;

$spacing-2:   4px;   // --space-2  — separador mínimo (ícone + texto)
$spacing-4:   8px;   // --space-4  — padding inline de chip/badge
$spacing-6:  12px;   // --space-6  — padding vertical de input
$spacing-8:  16px;   // --space-8  — padding padrão de card, gap de campos
$spacing-12: 24px;   // --space-12 — padding de seção
$spacing-16: 32px;   // --space-16 — espaço entre seções maiores
$spacing-24: 48px;   // --space-24 — padding de página
$spacing-32: 64px;   // --space-32 — espaço de empty states

:root {
  --space-2:  #{$spacing-2};
  --space-4:  #{$spacing-4};
  --space-6:  #{$spacing-6};
  --space-8:  #{$spacing-8};
  --space-12: #{$spacing-12};
  --space-16: #{$spacing-16};
  --space-24: #{$spacing-24};
  --space-32: #{$spacing-32};
}
```

### 3.2 Aplicação de Espaçamentos

| Contexto | Espaçamento |
|----------|------------|
| Ícone + label inline | `--space-2` (4px) |
| Gap entre chips/badges | `--space-2` (4px) |
| Padding de chip/badge | `4px 8px` |
| Padding interno de card | `--space-8` (16px) a `--space-12` (24px) |
| Gap entre campos de formulário | `--space-8` (16px) |
| Gap entre cards | `--space-8` (16px) |
| Padding lateral da página | `--space-12` (24px) em desktop, `--space-8` (16px) em mobile |
| Separação entre seções | `--space-16` (32px) |
| Padding do container principal | `--space-12` (24px) |
| Altura de linha de tabela | 48px (confortável) ou 40px (compacto) |

### 3.3 Bordas e Raios

```scss
$border-radius-sm: 4px;   // Chips, badges, inputs
$border-radius-md: 8px;   // Cards, painéis
$border-radius-lg: 12px;  // Modais, drawers
$border-radius-xl: 16px;  // Bottom sheets mobile
$border-radius-pill: 100px; // Chips de filtro arredondados

$border-color-default: var(--color-neutral-300);
$border-color-subtle:  rgba(0, 0, 0, 0.08);
$border-color-focus:   var(--color-primary-600);
```

### 3.4 Elevação (Sombras)

```scss
// Elevation baseada em Material Design
$elevation-0: none;
$elevation-1: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.08);
$elevation-2: 0 3px 6px rgba(0,0,0,0.10), 0 2px 4px rgba(0,0,0,0.08);
$elevation-3: 0 8px 16px rgba(0,0,0,0.10), 0 4px 8px rgba(0,0,0,0.06);
$elevation-4: 0 16px 32px rgba(0,0,0,0.12), 0 8px 16px rgba(0,0,0,0.08);
```

| Componente | Elevation |
|------------|-----------|
| Card padrão | `elevation-1` |
| Card hover | `elevation-2` |
| Sidebar | `elevation-2` |
| Dialog / Modal | `elevation-4` |
| Snackbar | `elevation-3` |

---

## 4. Grid System e Layout

### 4.1 Breakpoints

```scss
// styles/00-settings/_breakpoints.scss
$breakpoints: (
  'xs':  0px,      // Mobile portrait
  'sm':  600px,    // Mobile landscape / tablet portrait
  'md':  960px,    // Tablet landscape / small desktop
  'lg':  1280px,   // Desktop
  'xl':  1920px,   // Large desktop
);

@mixin respond-to($bp) {
  @if $bp == 'sm' { @media (min-width: 600px)  { @content; } }
  @if $bp == 'md' { @media (min-width: 960px)  { @content; } }
  @if $bp == 'lg' { @media (min-width: 1280px) { @content; } }
  @if $bp == 'xl' { @media (min-width: 1920px) { @content; } }
}
```

### 4.2 Layout Base

O SICO utiliza layout de **shell corporativo** com sidebar lateral fixa e área de conteúdo principal.

```
┌─────────────────────────────────────────────────────────────┐
│  HEADER (height: 64px)                                      │
│  [Logo] [Sistema SICO]                   [User] [Settings] │
├──────────────┬──────────────────────────────────────────────┤
│              │                                              │
│   SIDEBAR    │   MAIN CONTENT AREA                         │
│   (240px)    │   (flex-grow: 1)                            │
│              │                                              │
│  [Nav Item]  │   ┌──────────────────────────────────────┐  │
│  [Nav Item]  │   │  PAGE HEADER                         │  │
│  [Nav Item]  │   │  Breadcrumb + Título + Actions       │  │
│              │   └──────────────────────────────────────┘  │
│              │                                              │
│              │   ┌──────────────────────────────────────┐  │
│              │   │  CONTENT                             │  │
│              │   │  (cards, forms, tables)              │  │
│              │   └──────────────────────────────────────┘  │
│              │                                              │
└──────────────┴──────────────────────────────────────────────┘
```

### 4.3 Dimensões do Shell

| Elemento | Desktop (≥960px) | Mobile (<960px) |
|----------|-----------------|-----------------|
| Header | 64px de altura | 56px de altura |
| Sidebar | 240px fixo | Drawer sobreposto (256px) |
| Padding lateral do conteúdo | 24px | 16px |
| Max-width do conteúdo | 1200px | 100% |

### 4.4 Grid de Conteúdo

```scss
// 12 colunas com gap de 16px
.content-grid {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  gap: var(--space-8); // 16px
}

// Tamanhos de coluna de card
.col-12 { grid-column: span 12; }
.col-8  { grid-column: span 8; }
.col-6  { grid-column: span 6; }
.col-4  { grid-column: span 4; }
.col-3  { grid-column: span 3; }

// Mobile: tudo em 12 colunas
@media (max-width: 959px) {
  .col-8, .col-6, .col-4, .col-3 { grid-column: span 12; }
}
```

### 4.5 Estrutura de Página

Toda página segue o padrão:

```
Page Header (Breadcrumb + Título + Ações globais)
  └── Content Area
        ├── Filtros / Controles (se houver)
        ├── Content Principal (formulário, tabela, cards)
        └── Actions Secundárias (botões inferiores)
```

---

## 5. Componentes Visuais

### 5.1 Cards

Cards são a unidade de agrupamento de conteúdo. Existem três variantes:

**Card Padrão** — uso geral para formulários e resultados:
```scss
.card {
  background: var(--color-white);
  border-radius: $border-radius-md;   // 8px
  box-shadow: $elevation-1;
  padding: var(--space-12);           // 24px
  border: 1px solid $border-color-subtle;
}
```

**Card de Resultado** — exibe a classificação retornada pela IA:
```scss
.card--result {
  @extend .card;
  border-left: 4px solid var(--color-primary-600);

  &.severity-high     { border-left-color: var(--severity-high-main); }
  &.severity-critical { border-left-color: var(--severity-critical-main); }
  &.severity-medium   { border-left-color: var(--severity-medium-main); }
  &.severity-low      { border-left-color: var(--severity-low-main); }
}
```

**Card de Seção Interna** — agrupa subcampos dentro de um card principal:
```scss
.card--section {
  background: var(--color-neutral-50);
  border-radius: $border-radius-sm;   // 4px
  padding: var(--space-8);            // 16px
  border: 1px solid var(--color-neutral-300);
}
```

### 5.2 Badges de Severidade

```html
<!-- Uso no template Angular -->
<span class="severity-badge" [ngClass]="'severity-badge--' + severity.toLowerCase()">
  <mat-icon>{{ severityIcon }}</mat-icon>
  {{ severityLabel }}
</span>
```

```scss
.severity-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: 3px 10px;
  border-radius: $border-radius-pill;
  font-size: 12px;
  font-weight: 500;
  letter-spacing: 0.04em;
  white-space: nowrap;

  mat-icon { font-size: 14px; width: 14px; height: 14px; }

  &--low      { background: var(--severity-low-bg);      color: var(--severity-low-text); }
  &--medium   { background: var(--severity-medium-bg);   color: var(--severity-medium-text); }
  &--high     { background: var(--severity-high-bg);     color: var(--severity-high-text); }
  &--critical { background: var(--severity-critical-bg); color: var(--severity-critical-text); }
}
```

**Mapeamento de ícones de severidade:**

| Severidade | Ícone Material | Label PT |
|------------|---------------|----------|
| `LOW` | `arrow_downward` | Baixa |
| `MEDIUM` | `remove` | Média |
| `HIGH` | `arrow_upward` | Alta |
| `CRITICAL` | `priority_high` | Crítica |

### 5.3 Badges de Categoria

```html
<span class="category-badge" [ngClass]="'category-badge--' + category.toLowerCase()">
  <mat-icon>{{ categoryIcon }}</mat-icon>
  {{ categoryLabel }}
</span>
```

```scss
.category-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: 3px 10px;
  border-radius: $border-radius-sm;  // Ângulo reto (diferencia de severity)
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;

  mat-icon { font-size: 14px; width: 14px; height: 14px; }

  // Gerado via loop $categories map
  @each $cat, $colors in $categories {
    $main: nth($colors, 1);
    $bg:   nth($colors, 2);
    &--#{to-lower-case($cat)} {
      background-color: $bg;
      color: darken($main, 10%);
    }
  }
}
```

> **Regra de diferenciação:** Badges de severidade têm bordas pílula (arredondadas) para remeter a "nível/escala". Badges de categoria têm bordas levemente retas (4px) para remeter a "tipo/classificação".

### 5.4 Painel de Resultado da Classificação

Layout esperado após submissão bem-sucedida:

```
┌────────────────────────────────────────────────────────┐
│ ✓  Incidente Registrado com Sucesso          #001      │  ← Header com ID
├────────────────────────────────────────────────────────┤
│                                                        │
│  TEXTO FORMALIZADO PELA IA                             │
│  ┌──────────────────────────────────────────────────┐  │
│  │ O banco de dados principal encontra-se           │  │  ← Fonte mono
│  │ indisponível desde as 08:00...                   │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
│  [DATABASE  ] [● ALTA ]    Registrado: 19/05 14:30    │  ← Badges + meta
│                                                        │
│  AÇÃO RECOMENDADA                                      │
│  ┌──────────────────────────────────────────────────┐  │
│  │ 1. Verificar status do banco de dados            │  │
│  │ 2. Revisar logs dos últimos 30 minutos           │  │
│  │ 3. Executar failover se necessário               │  │
│  └──────────────────────────────────────────────────┘  │
│                                                        │
│  [Novo Incidente]            [Ver Histórico  →]        │  ← CTAs
└────────────────────────────────────────────────────────┘
```

### 5.5 Empty State

Para histórico sem registros:

```
┌────────────────────────────────────────────────────────┐
│                                                        │
│              [ícone inbox  48px]                       │
│                                                        │
│           Nenhum incidente registrado                  │
│    Registre o primeiro incidente para começar.        │
│                                                        │
│              [+ Registrar Incidente]                   │
│                                                        │
└────────────────────────────────────────────────────────┘
```

```scss
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--space-6);
  padding: var(--space-32) var(--space-16);
  text-align: center;

  .empty-state__icon {
    font-size: 48px;
    color: var(--color-neutral-500);
  }

  .empty-state__title   { @extend %text-headline-3; color: var(--color-neutral-700); }
  .empty-state__message { @extend %text-body-medium; color: var(--color-neutral-500); }
}
```

---

## 6. Formulários

### 6.1 Princípios de Formulário

- **Um objetivo por formulário:** O formulário de registro tem apenas um campo funcional (descrição). Não adicionar campos que desviem do fluxo.
- **Validação progressiva:** Validar em tempo real após o primeiro toque (`blur`), não na digitação inicial.
- **Feedback inline:** Mensagens de erro sempre abaixo do campo que originou o problema.
- **Estado do botão:** Botão de envio desabilitado enquanto o formulário é inválido. Nunca exibir erro só ao tentar submeter.

### 6.2 Campo de Descrição (Textarea Principal)

```html
<mat-form-field appearance="outline" class="incident-field">
  <mat-label>Descrição do incidente</mat-label>
  <textarea
    matInput
    formControlName="incident"
    [rows]="6"
    placeholder="Descreva o incidente com o máximo de detalhes: o que aconteceu, quando, impacto observado e sistemas afetados."
    maxlength="5000">
  </textarea>
  <mat-hint align="end">{{ charCount }}/5000</mat-hint>
  <mat-error *ngIf="form.get('incident')?.hasError('required')">
    A descrição é obrigatória.
  </mat-error>
  <mat-error *ngIf="form.get('incident')?.hasError('minlength')">
    Mínimo de 10 caracteres.
  </mat-error>
  <mat-error *ngIf="form.get('incident')?.hasError('maxlength')">
    Máximo de 5.000 caracteres.
  </mat-error>
</mat-form-field>
```

```scss
.incident-field {
  width: 100%;

  textarea {
    font-family: $font-family-base;
    font-size: 14px;
    line-height: 1.6;
    resize: vertical;
    min-height: 120px;
  }
}
```

### 6.3 Estados de Formulário

| Estado | Comportamento Visual |
|--------|---------------------|
| **Default** | Borda `neutral-300`, label flutuante acima |
| **Focus** | Borda `primary-600` (2px), label azul |
| **Error** | Borda `#b71c1c` (2px), label + mensagem vermelhos |
| **Disabled** | Fundo `neutral-100`, texto `neutral-500`, não interagível |
| **Loading** | Campo desabilitado + spinner no botão |

### 6.4 Contador de Caracteres

```scss
mat-hint {
  &[align="end"] {
    font-family: $font-family-mono;
    font-size: 11px;
    color: var(--color-neutral-500);

    // Alerta quando próximo do limite
    &.near-limit   { color: var(--severity-medium-main); }
    &.at-limit     { color: var(--severity-high-main); font-weight: 500; }
  }
}
```

- `> 4500`: cor normal
- `4500–4900`: `near-limit` (amarelo/laranja)
- `≥ 4901`: `at-limit` (vermelho)

### 6.5 Dicas de Preenchimento

Exibir como `mat-hint` abaixo do campo (não modal, não tooltip):

```
Dica: Inclua o horário do incidente, sistemas afetados e impacto observado.
Exemplo: "API de autenticação retornando erro 503 desde as 09:15. Todos os
usuários não conseguem fazer login."
```

---

## 7. Botões

### 7.1 Hierarquia de Botões

| Variante | Angular Material | Uso |
|----------|-----------------|-----|
| **Primary** | `mat-raised-button color="primary"` | Ação principal da página (1 por seção) |
| **Secondary** | `mat-stroked-button` | Ação secundária (Cancelar, Voltar) |
| **Tertiary** | `mat-button` | Ação de menor peso (Ver histórico) |
| **Danger** | `mat-raised-button color="warn"` | Ações destrutivas ou críticas |
| **Icon** | `mat-icon-button` | Ações compactas (copiar, filtrar) |

### 7.2 Regras de Uso

- **Máximo de 1 botão primário por área de ação.** Nunca dois `raised-button` no mesmo grupo.
- **Ordem:** Ação secundária à esquerda, ação primária à direita (padrão corporativo).
- **Largura mínima:** 88px. Nunca botão menor que o texto que contém + padding.
- **Sem ALL CAPS:** `text-transform: none`. Texto do botão em formato sentença (primeira maiúscula).
- **Loading state:** Substituir texto por spinner inline + texto "Processando..."

### 7.3 Estados de Botão

```scss
.btn-primary {
  // Default
  background-color: var(--color-primary-600);
  color: white;

  // Hover
  &:hover:not(:disabled) { background-color: var(--color-primary-700); }

  // Active
  &:active:not(:disabled) { background-color: var(--color-primary-900); }

  // Disabled
  &:disabled {
    background-color: var(--color-neutral-300);
    color: var(--color-neutral-500);
    cursor: not-allowed;
  }

  // Loading
  &.loading {
    pointer-events: none;
    opacity: 0.85;

    .btn-content { visibility: hidden; }
    .btn-spinner {
      position: absolute;
      inset: 0;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
```

### 7.4 Botão com Loading State (Template)

```html
<button
  mat-raised-button
  color="primary"
  type="submit"
  [disabled]="!form.valid || (loading$ | async)"
  [class.loading]="loading$ | async">
  <span class="btn-content">
    <mat-icon>send</mat-icon>
    Classificar Incidente
  </span>
  <span class="btn-spinner" *ngIf="loading$ | async">
    <mat-spinner diameter="20" strokeWidth="2"></mat-spinner>
    &nbsp;Processando...
  </span>
</button>
```

---

## 8. Tabelas e Listas

### 8.1 Estrutura da Tabela de Histórico

```html
<mat-table [dataSource]="incidents$" matSort>

  <!-- Coluna: Data/Hora -->
  <ng-container matColumnDef="registeredAt">
    <mat-header-cell *matHeaderCellDef mat-sort-header>Data</mat-header-cell>
    <mat-cell *matCellDef="let row">
      <span class="timestamp">{{ row.registeredAt | date:'dd/MM/yy HH:mm' }}</span>
    </mat-cell>
  </ng-container>

  <!-- Coluna: Categoria -->
  <ng-container matColumnDef="category">
    <mat-header-cell *matHeaderCellDef>Categoria</mat-header-cell>
    <mat-cell *matCellDef="let row">
      <app-category-badge [category]="row.category"></app-category-badge>
    </mat-cell>
  </ng-container>

  <!-- Coluna: Severidade -->
  <ng-container matColumnDef="severity">
    <mat-header-cell *matHeaderCellDef mat-sort-header>Severidade</mat-header-cell>
    <mat-cell *matCellDef="let row">
      <app-severity-badge [severity]="row.severity"></app-severity-badge>
    </mat-cell>
  </ng-container>

  <!-- Coluna: Descrição (truncada) -->
  <ng-container matColumnDef="description">
    <mat-header-cell *matHeaderCellDef>Descrição</mat-header-cell>
    <mat-cell *matCellDef="let row">
      <span class="text-truncate" [matTooltip]="row.formalizedIncidentText">
        {{ row.formalizedIncidentText | slice:0:100 }}{{ row.formalizedIncidentText.length > 100 ? '...' : '' }}
      </span>
    </mat-cell>
  </ng-container>

  <!-- Coluna: Ação -->
  <ng-container matColumnDef="action">
    <mat-header-cell *matHeaderCellDef></mat-header-cell>
    <mat-cell *matCellDef="let row">
      <button mat-icon-button [routerLink]="['/incident', row.id]" matTooltip="Ver detalhes">
        <mat-icon>chevron_right</mat-icon>
      </button>
    </mat-cell>
  </ng-container>

  <mat-header-row *matHeaderRowDef="displayedColumns; sticky: true"></mat-header-row>
  <mat-row *matRowDef="let row; columns: displayedColumns;"
           [class.row--high]="row.severity === 'HIGH'"
           [class.row--critical]="row.severity === 'CRITICAL'">
  </mat-row>

</mat-table>
```

### 8.2 Estilos da Tabela

```scss
mat-table {
  width: 100%;
  background: var(--color-white);

  mat-header-row {
    height: 48px;
    background: var(--color-neutral-50);
    border-bottom: 2px solid var(--color-neutral-300);
  }

  mat-header-cell {
    font-size: 11px;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: var(--color-neutral-700);
  }

  mat-row {
    height: 52px;
    border-bottom: 1px solid var(--color-neutral-100);
    transition: background-color 0.15s ease;

    &:hover { background-color: var(--color-primary-50); }

    // Destaque para incidentes críticos
    &.row--critical {
      background-color: #fff5f5;
      &:hover { background-color: #ffe8e8; }
    }

    &.row--high {
      background-color: #fff8f5;
    }
  }

  mat-cell {
    font-size: 14px;
    color: var(--color-neutral-900);
    padding: 0 var(--space-4) 0 var(--space-6);
  }

  .text-truncate {
    max-width: 300px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: block;
  }

  .timestamp {
    font-family: $font-family-mono;
    font-size: 12px;
    color: var(--color-neutral-700);
    white-space: nowrap;
  }
}
```

### 8.3 Paginação

```html
<mat-paginator
  [pageSize]="20"
  [pageSizeOptions]="[10, 20, 50]"
  [showFirstLastButtons]="true"
  aria-label="Selecionar página do histórico">
</mat-paginator>
```

```scss
mat-paginator {
  border-top: 1px solid var(--color-neutral-300);
  font-size: 13px;

  .mat-mdc-select-value { font-size: 13px; }
}
```

### 8.4 Ordenação e Filtros

- **Colunas ordenáveis:** `registeredAt`, `severity`. Indicador `↑↓` no cabeçalho.
- **Ordenação padrão:** `registeredAt DESC` (mais recentes primeiro).
- **Filtros disponíveis (Fase 2+):** categoria, severidade, período. Exibidos como chips removíveis acima da tabela.

---

## 9. Feedback Visual

### 9.1 Estados de Loading

#### Loading de Classificação (IA em Processamento)

Contexto: operador clicou em "Classificar" e aguarda a resposta (pode levar 2–5 segundos).

```html
<div class="ai-processing-overlay" *ngIf="loading$ | async">
  <mat-spinner diameter="40" strokeWidth="3"></mat-spinner>
  <p class="ai-processing__title">Analisando incidente...</p>
  <p class="ai-processing__subtitle">Processamento de IA em andamento</p>
</div>
```

```scss
.ai-processing-overlay {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-6);
  padding: var(--space-16);
  background: rgba(255, 255, 255, 0.9);
  border-radius: $border-radius-md;
  border: 1px solid var(--color-primary-100);

  &__title    { @extend %text-title-medium; color: var(--color-primary-600); margin: 0; }
  &__subtitle { @extend %text-body-small;  color: var(--color-neutral-500); margin: 0; }
}
```

#### Loading de Tabela (Skeleton)

Para o carregamento inicial da tabela de histórico:

```html
<div class="skeleton-row" *ngFor="let i of [1,2,3,4,5]">
  <div class="skeleton-cell skeleton-cell--date"></div>
  <div class="skeleton-cell skeleton-cell--badge"></div>
  <div class="skeleton-cell skeleton-cell--badge"></div>
  <div class="skeleton-cell skeleton-cell--text"></div>
</div>
```

```scss
.skeleton-cell {
  height: 20px;
  border-radius: $border-radius-sm;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-shimmer 1.5s infinite;

  &--date  { width: 100px; }
  &--badge { width: 80px; }
  &--text  { width: 100%; max-width: 280px; }
}

@keyframes skeleton-shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}
```

### 9.2 Snackbar / Notificações Toast

Implementar via `MatSnackBar` com classes customizadas:

```typescript
// notification.service.ts
private show(message: string, type: 'success' | 'error' | 'warning' | 'info'): void {
  this.snackBar.open(message, 'Fechar', {
    duration: type === 'error' ? 6000 : 4000,
    horizontalPosition: 'end',
    verticalPosition: 'bottom',
    panelClass: [`snack--${type}`]
  });
}
```

```scss
// Snackbar customizado (global styles)
.snack--success {
  --mdc-snackbar-container-color: #1b5e20;
  --mdc-snackbar-supporting-text-color: white;
  .mat-mdc-button.mdc-button { color: #a5d6a7; }
}

.snack--error {
  --mdc-snackbar-container-color: #b71c1c;
  --mdc-snackbar-supporting-text-color: white;
  .mat-mdc-button.mdc-button { color: #ef9a9a; }
}

.snack--warning {
  --mdc-snackbar-container-color: #e65100;
  --mdc-snackbar-supporting-text-color: white;
  .mat-mdc-button.mdc-button { color: #ffcc80; }
}

.snack--info {
  --mdc-snackbar-container-color: #0d47a1;
  --mdc-snackbar-supporting-text-color: white;
  .mat-mdc-button.mdc-button { color: #90caf9; }
}
```

**Conteúdo das notificações:**

| Evento | Tipo | Mensagem |
|--------|------|----------|
| Classificação concluída | success | "Incidente classificado e registrado com sucesso." |
| Erro de rede | error | "Falha na conexão. Verifique sua rede e tente novamente." |
| Serviço de IA indisponível | error | "Serviço de classificação indisponível. Tente em alguns instantes." |
| Erro de validação (backend) | warning | "Descrição inválida. Verifique o campo e tente novamente." |
| Erro interno do servidor | error | "Erro interno do servidor. Contate o suporte se persistir." |
| Histórico carregado | info | (silencioso — não exibir notificação para GET bem-sucedido) |

### 9.3 Mensagens de Erro Inline

Para erros críticos que impedem o uso da página:

```html
<div class="error-banner" role="alert" *ngIf="pageError$ | async as error">
  <mat-icon>error_outline</mat-icon>
  <div class="error-banner__content">
    <strong>{{ error.title }}</strong>
    <span>{{ error.message }}</span>
  </div>
  <button mat-icon-button (click)="dismissError()" aria-label="Fechar aviso">
    <mat-icon>close</mat-icon>
  </button>
</div>
```

```scss
.error-banner {
  display: flex;
  align-items: center;
  gap: var(--space-6);
  padding: var(--space-6) var(--space-8);
  background-color: #ffebee;
  border-left: 4px solid #b71c1c;
  border-radius: $border-radius-sm;
  margin-bottom: var(--space-8);

  mat-icon { color: #b71c1c; flex-shrink: 0; }

  &__content {
    flex: 1;
    strong  { display: block; font-size: 14px; font-weight: 500; color: #7f0000; }
    span    { font-size: 13px; color: #b71c1c; }
  }
}
```

### 9.4 Progress Linear

Para operações longas com feedback de progresso (futuro/upload):

```scss
mat-progress-bar {
  --mdc-linear-progress-active-indicator-color: var(--color-primary-600);
  --mdc-linear-progress-track-color: var(--color-primary-100);
  border-radius: 2px;
  height: 3px;
}
```

---

## 10. Responsividade

### 10.1 Abordagem Mobile-First

O SICO é primariamente desktop (operadores técnicos), mas deve ser funcional em tablets e mobile para consultas de histórico em campo.

**Prioridade de experiência:**
1. Desktop ≥1280px — experiência completa
2. Desktop 960–1279px — experiência completa (sidebar pode comprimir)
3. Tablet 600–959px — sidebar como drawer; tabela com colunas reduzidas
4. Mobile <600px — sidebar oculta (hamburger); formulário full-width; tabela em cards

### 10.2 Sidebar Responsiva

```typescript
// app.component.ts
isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset)
  .pipe(map(result => result.matches));
```

```html
<mat-sidenav-container>
  <mat-sidenav
    [mode]="(isHandset$ | async) ? 'over' : 'side'"
    [opened]="!(isHandset$ | async)"
    fixedInViewport>
    <app-sidebar></app-sidebar>
  </mat-sidenav>

  <mat-sidenav-content>
    <app-header (menuToggle)="sidenav.toggle()"></app-header>
    <main class="page-content">
      <router-outlet></router-outlet>
    </main>
  </mat-sidenav-content>
</mat-sidenav-container>
```

### 10.3 Tabela Responsiva (Mobile)

Em telas < 600px, a tabela de histórico colapsa para lista de cards:

```scss
@media (max-width: 599px) {
  mat-table {
    // Ocultar colunas menos essenciais
    .mat-column-description { display: none; }

    mat-row {
      height: auto;
      flex-direction: column;
      padding: var(--space-6) var(--space-8);
      border-bottom: 1px solid var(--color-neutral-300);
    }

    mat-cell {
      padding: 2px 0;
      font-size: 13px;
      border: none;

      // Mostrar label antes do valor
      &::before {
        content: attr(data-label);
        font-weight: 500;
        font-size: 11px;
        color: var(--color-neutral-500);
        display: block;
        text-transform: uppercase;
        letter-spacing: 0.05em;
      }
    }
  }
}
```

### 10.4 Formulário Responsivo

```scss
.incident-form {
  max-width: 800px;
  width: 100%;

  .form-actions {
    display: flex;
    gap: var(--space-4);
    justify-content: flex-end;
    flex-wrap: wrap;

    @media (max-width: 599px) {
      flex-direction: column-reverse;
      button { width: 100%; }
    }
  }
}
```

### 10.5 Checklist de Responsividade

- [ ] Header: hamburger visível em mobile, logo truncado
- [ ] Sidebar: drawer em mobile, fixo em desktop
- [ ] Formulário: full-width em mobile, limitado a 800px em desktop
- [ ] Botões: full-width em mobile, auto em desktop
- [ ] Tabela: colunas visíveis adequadas por breakpoint
- [ ] Cards: single-column em mobile, grid em desktop
- [ ] Snackbar: full-width em mobile, limitado em desktop
- [ ] Touch targets: mínimo 48x48px para elementos interativos

---

## 11. Acessibilidade

### 11.1 Conformidade

O SICO deve atingir **WCAG 2.1 nível AA** em todas as telas funcionais.

### 11.2 Contraste de Cores

| Combinação | Razão de Contraste | Status |
|------------|-------------------|--------|
| Texto principal (#212121) sobre branco | 16.1:1 | ✅ AAA |
| Texto secondary (#424242) sobre branco | 9.7:1 | ✅ AAA |
| Texto em chip LOW (#1b5e20) sobre bg (#e8f5e9) | 4.8:1 | ✅ AA |
| Texto em chip HIGH (#bf360c) sobre bg (#fbe9e7) | 4.6:1 | ✅ AA |
| Botão primary (#1976d2) sobre branco | 4.6:1 | ✅ AA |
| Texto de placeholder (#9e9e9e) sobre branco | 2.8:1 | ⚠️ (apenas decorativo) |

### 11.3 Navegação por Teclado

- Todo elemento interativo alcançável via `Tab` e `Shift+Tab`
- Ordem de foco lógica: Header → Sidebar → Conteúdo principal → Footer
- Focus ring visível em todos os elementos: `outline: 2px solid var(--color-primary-600)`
- Atalho `Skip to content` como primeiro elemento focável:

```html
<a class="skip-link" href="#main-content">Ir para o conteúdo principal</a>
```

```scss
.skip-link {
  position: absolute;
  top: -40px;
  left: 0;
  padding: var(--space-4) var(--space-8);
  background: var(--color-primary-600);
  color: white;
  font-size: 14px;
  font-weight: 500;
  z-index: 1000;
  text-decoration: none;
  border-radius: 0 0 $border-radius-sm $border-radius-sm;

  &:focus { top: 0; }
}
```

### 11.4 Atributos ARIA

```html
<!-- Região principal -->
<main id="main-content" role="main" aria-label="Conteúdo principal">

<!-- Formulário com contexto -->
<form role="form" aria-label="Registrar novo incidente">
  <textarea aria-describedby="incident-hint incident-error" aria-required="true"></textarea>
  <div id="incident-hint">Descreva o incidente em linguagem natural (mínimo 10 caracteres)</div>
  <div id="incident-error" role="alert" aria-live="polite"></div>
</form>

<!-- Tabela com caption -->
<mat-table role="table" aria-label="Histórico de incidentes registrados">
  <caption>Lista de incidentes mais recentes</caption>
</mat-table>

<!-- Botão com estado de carregamento -->
<button aria-busy="true" aria-label="Classificando incidente, aguarde...">
  <mat-spinner diameter="20"></mat-spinner>
</button>

<!-- Badges com contexto -->
<span role="status" aria-label="Severidade: Alta">
  <app-severity-badge severity="HIGH"></app-severity-badge>
</span>
```

### 11.5 Gerenciamento de Foco

- Após submissão de formulário com sucesso: foco vai para o card de resultado
- Após erro de API: foco vai para o banner de erro
- Ao abrir modal/dialog: foco vai para o primeiro elemento interativo do dialog
- Ao fechar modal: foco retorna ao elemento que o abriu

```typescript
// Exemplo: focar resultado após classificação
@ViewChild('resultCard') resultCard: ElementRef;

onClassificationSuccess(): void {
  setTimeout(() => {
    this.resultCard.nativeElement.focus();
  }, 100);
}
```

### 11.6 Modo de Alto Contraste

```scss
@media (forced-colors: active) {
  .severity-badge, .category-badge {
    forced-color-adjust: none;
    border: 1px solid ButtonText;
  }

  mat-table mat-row.row--critical {
    outline: 2px solid CanvasText;
  }
}
```

---

## 12. Dark Mode

### 12.1 Estratégia

Dark mode via CSS custom properties + classe `dark-theme` no `<body>`. Angular Material suporta dois temas compilados; alternar via `OverlayContainer`.

### 12.2 Paleta Dark

```scss
// styles/themes/_dark.scss
.dark-theme {
  // Superfícies
  --color-surface-bg:    #121212;
  --color-surface-card:  #1e1e1e;
  --color-surface-panel: #252525;
  --color-surface-hover: #2c2c2c;

  // Textos
  --color-text-primary:   rgba(255, 255, 255, 0.87);
  --color-text-secondary: rgba(255, 255, 255, 0.60);
  --color-text-disabled:  rgba(255, 255, 255, 0.38);

  // Bordas
  --border-color-default: rgba(255, 255, 255, 0.12);

  // Primária (mais suave no escuro)
  --color-primary-600: #90caf9;  // Texto azul sobre escuro
  --color-primary-50:  rgba(144, 202, 249, 0.08);

  // Severidades (ajustadas para contraste em escuro)
  --severity-low-bg:        rgba(46, 125, 50, 0.15);
  --severity-low-text:      #a5d6a7;
  --severity-medium-bg:     rgba(245, 127, 23, 0.15);
  --severity-medium-text:   #ffcc80;
  --severity-high-bg:       rgba(230, 74, 25, 0.15);
  --severity-high-text:     #ffab91;
  --severity-critical-bg:   rgba(183, 28, 28, 0.15);
  --severity-critical-text: #ef9a9a;
}
```

### 12.3 Tema Angular Material para Dark

```scss
// styles/themes/_material-dark.scss
@use '@angular/material' as mat;

$dark-primary:  mat.define-palette(mat.$blue-palette, 300);
$dark-accent:   mat.define-palette(mat.$pink-palette, A100);
$dark-warn:     mat.define-palette(mat.$red-palette, 300);

$dark-theme: mat.define-dark-theme((
  color: (
    primary: $dark-primary,
    accent:  $dark-accent,
    warn:    $dark-warn,
  ),
  typography: mat.define-typography-config(),
  density: 0,
));

.dark-theme {
  @include mat.all-component-colors($dark-theme);
}
```

### 12.4 Toggle de Dark Mode

```typescript
// theme.service.ts
@Injectable({ providedIn: 'root' })
export class ThemeService {
  private isDark = signal(false);

  constructor(private overlayContainer: OverlayContainer) {
    const saved = localStorage.getItem('theme');
    if (saved === 'dark') this.enableDark();
  }

  toggle(): void {
    this.isDark() ? this.enableLight() : this.enableDark();
  }

  private enableDark(): void {
    this.isDark.set(true);
    document.body.classList.add('dark-theme');
    this.overlayContainer.getContainerElement().classList.add('dark-theme');
    localStorage.setItem('theme', 'dark');
  }

  private enableLight(): void {
    this.isDark.set(false);
    document.body.classList.remove('dark-theme');
    this.overlayContainer.getContainerElement().classList.remove('dark-theme');
    localStorage.setItem('theme', 'light');
  }
}
```

### 12.5 Verificações Obrigatórias no Dark Mode

- [ ] Todos os textos mantêm contraste ≥ 4.5:1
- [ ] Badges de severidade legíveis sobre fundos escuros
- [ ] Cards têm elevação perceptível (bordas sutis, não sombras)
- [ ] Imagens/ícones não ficam "invisíveis"
- [ ] Snackbar contrasta com background da página
- [ ] Skeleton loaders adaptados para escuro

---

## 13. Boas Práticas UX

### 13.1 Princípios Fundamentais

**Clareza sobre estética.** Operadores técnicos em situações de stress precisam encontrar e ler informações imediatamente. A interface não deve exigir aprendizado.

**Densidade informacional calibrada.** A tabela de histórico exibe múltiplos campos. Priorizar densidade com espaçamento mínimo de conforto — não espaçamento de marketing.

**Ação requer confirmação, não navegação.** O fluxo principal (registrar incidente → ver resultado) ocorre em uma única tela, sem mudança de página.

**Erros são informativos, não punitivos.** Mensagens de erro guiam o próximo passo. Nunca apenas "Erro". Sempre: o que aconteceu + o que fazer.

### 13.2 Comportamento Esperado da Interface

#### Formulário de Registro

| Ação do Usuário | Comportamento da Interface |
|----------------|---------------------------|
| Página carrega | Campo de texto em foco automático, botão desabilitado |
| Digita < 10 chars | Contador atualiza, botão permanece desabilitado |
| Digita ≥ 10 chars | Botão é habilitado. Sem mensagem de erro prematura |
| Perde foco com campo inválido | Mensagem de erro aparece abaixo do campo |
| Clica em Classificar | Botão muda para "Processando...", campo fica disabled, spinner aparece |
| Resposta de sucesso chega | Spinner some, resultado aparece abaixo do formulário, snackbar verde |
| Resposta de erro chega | Spinner some, campo volta a enabled, snackbar vermelho, banner de erro opcional |
| Clica "Novo Incidente" | Formulário é resetado, campo em foco, resultado some |

#### Tabela de Histórico

| Ação do Usuário | Comportamento da Interface |
|----------------|---------------------------|
| Navega para aba Histórico | Skeleton exibido, GET disparado automaticamente |
| Dados chegam | Skeleton substituído pela tabela |
| Clica no cabeçalho "Data" | Ordenação alterna ASC/DESC, seta indicadora atualiza |
| Passa mouse sobre linha | Fundo azul suave (`primary-50`) |
| Clica no chevron da linha | Navega para tela de detalhe |
| 0 registros | Empty state centralizado com CTA |

### 13.3 Consistência Visual

- **Cores de ação primária:** sempre azul primário (`#1976d2`)
- **Cores de status:** sempre o mapeamento semântico definido (verde/amarelo/laranja/vermelho)
- **Ícones de categoria:** sempre os mapeados, nunca ícones alternativos
- **Formatos de data:** sempre `dd/MM/yy HH:mm` na tabela, `dd de MMMM de yyyy às HH:mm` no detalhe
- **Textos de loading:** sempre "Processando...", nunca "Aguarde", "Carregando", "Por favor aguarde"
- **Textos de botão:** sempre em português, sem abreviações

### 13.4 Fluxo do Usuário — Mapa Mental

```
ENTRADA NO SISTEMA
      │
      ▼
[Dashboard / Formulário de Registro]
      │
      ├─── Registrar Novo Incidente
      │         │
      │         ▼
      │    [Digita descrição] → [Valida] → [Submete]
      │         │
      │         ├── Sucesso → [Exibe Resultado] → [Novo / Histórico]
      │         └── Erro   → [Exibe Mensagem] → [Tenta novamente]
      │
      └─── Ver Histórico
                │
                ▼
          [Tabela de Incidentes]
                │
                ├── Ordena / Filtra
                ├── Pagina
                └── Seleciona linha → [Detalhe do Incidente]
```

### 13.5 Prevenção de Erros

- **Double-submit:** Botão desabilitado durante processamento. Nunca dois cliques possíveis.
- **Perda de conteúdo:** `UnsavedChangesGuard` ao tentar sair com formulário preenchido e não submetido.
- **Timeout visível:** Após 15s sem resposta, exibir mensagem: "A classificação está demorando mais que o esperado. Aguarde ou tente novamente."
- **Retry automático:** Implementar silenciosamente no `HttpClientService` (1 retry após falha).

### 13.6 Microcopy — Textos de Interface

Todos os textos de interface devem seguir o tom: **direto, técnico, sem jargão desnecessário**.

| Contexto | Texto Incorreto | Texto Correto |
|----------|-----------------|---------------|
| Placeholder do campo | "Digite aqui seu texto" | "Descreva o incidente com detalhes: o que aconteceu, quando e sistemas afetados." |
| Botão de envio | "Enviar" | "Classificar Incidente" |
| Loading | "Por favor aguarde..." | "Processando..." |
| Sucesso | "Operação realizada!" | "Incidente registrado com sucesso." |
| Erro de rede | "Algo deu errado" | "Falha na conexão com o servidor. Verifique sua rede." |
| Erro de IA | "Serviço indisponível" | "Serviço de classificação indisponível. Tente novamente em alguns instantes." |
| Empty state | "Sem dados" | "Nenhum incidente registrado. Registre o primeiro incidente para iniciar o histórico." |
| Histórico tab | "Histórico" | "Histórico de Incidentes" |

---

## 14. Padrões Angular Material

### 14.1 Configuração do Tema

```scss
// styles/theme.scss
@use '@angular/material' as mat;

@include mat.core();

$primary: mat.define-palette(mat.$blue-palette, 700, 400, 900);
$accent:  mat.define-palette(mat.$blue-grey-palette, 600);
$warn:    mat.define-palette(mat.$red-palette, 700);

$sico-theme: mat.define-light-theme((
  color: (
    primary: $primary,
    accent:  $accent,
    warn:    $warn,
  ),
  typography: mat.define-typography-config(
    $font-family: 'Roboto, sans-serif',
    $body-1: mat.define-typography-level(14px, 1.5, 400),
    $body-2: mat.define-typography-level(14px, 1.5, 500),
    $button: mat.define-typography-level(14px, 36px, 500, $letter-spacing: 0),
  ),
  density: 0,
));

@include mat.all-component-themes($sico-theme);
```

### 14.2 Componentes Material Utilizados

| Componente | Módulo | Uso no SICO |
|------------|--------|-------------|
| `MatFormField` | `MatFormFieldModule` | Campo de descrição do incidente |
| `MatInput` | `MatInputModule` | Textarea e inputs de filtro |
| `MatButton` | `MatButtonModule` | Todos os botões |
| `MatIconButton` | `MatButtonModule` | Ações de linha na tabela |
| `MatIcon` | `MatIconModule` | Iconografia geral |
| `MatTable` | `MatTableModule` | Tabela de histórico |
| `MatSort` | `MatSortModule` | Ordenação de colunas |
| `MatPaginator` | `MatPaginatorModule` | Paginação do histórico |
| `MatProgressSpinner` | `MatProgressSpinnerModule` | Loading de classificação |
| `MatProgressBar` | `MatProgressBarModule` | Loading de página |
| `MatSnackBar` | `MatSnackBarModule` | Notificações de status |
| `MatTooltip` | `MatTooltipModule` | Tooltips de truncamento |
| `MatSidenav` | `MatSidenavModule` | Layout shell com sidebar |
| `MatToolbar` | `MatToolbarModule` | Header da aplicação |
| `MatChips` | `MatChipsModule` | Filtros de tabela (Fase 2) |
| `MatDialog` | `MatDialogModule` | Confirmações e detalhes |
| `MatMenu` | `MatMenuModule` | Menu de usuário no header |
| `MatDivider` | `MatDividerModule` | Separadores visuais |
| `MatCard` | `MatCardModule` | Containers de conteúdo |
| `MatBadge` | `MatBadgeModule` | Contadores de notificação |

### 14.3 Customizações de Componentes Material

```scss
// Sobrescrever estilos Material sem hackear seletores internos
// Usar CSS Custom Properties (tokens) do Material Design 3

// mat-form-field — aparência outline
mat-form-field.mat-mdc-form-field {
  --mdc-outlined-text-field-container-shape: #{$border-radius-sm};
  --mdc-outlined-text-field-focus-outline-width: 2px;
}

// mat-button — remover uppercase
.mat-mdc-button,
.mat-mdc-raised-button,
.mat-mdc-outlined-button {
  --mdc-text-button-label-text-transform: none;
  --mdc-filled-button-label-text-transform: none;
  --mdc-outlined-button-label-text-transform: none;
  letter-spacing: 0;
}

// mat-table — ajustar fonte dos headers
.mat-mdc-header-cell {
  font-size: 11px !important;
  font-weight: 600 !important;
}

// mat-snack-bar — posicionar acima de elementos fixos
.mdc-snackbar {
  margin-bottom: env(safe-area-inset-bottom, 16px);
}
```

### 14.4 Uso de CDK

```typescript
// Breakpoints via CDK (substitui MediaQuery manual)
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

@Injectable({ providedIn: 'root' })
export class ResponsiveService {
  isMobile$ = this.observer.observe(Breakpoints.Handset)
    .pipe(map(r => r.matches));

  isTablet$ = this.observer.observe(Breakpoints.Tablet)
    .pipe(map(r => r.matches));

  constructor(private observer: BreakpointObserver) {}
}
```

### 14.5 Importação Standalone de Módulos Material

```typescript
// Em standalone components, importar apenas o que é usado
@Component({
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    // NÃO importar MatTableModule, MatSortModule aqui se não usar
  ]
})
export class IncidentFormComponent {}
```

---

## Apêndice A — Checklist de Implementação

### Design System

- [ ] Variáveis SCSS de cores, espaçamentos e tipografia definidas
- [ ] Tema Angular Material configurado e funcionando
- [ ] Dark mode implementado com toggle persistido
- [ ] Breakpoints e mixins responsivos disponíveis

### Componentes

- [ ] `SeverityBadgeComponent` com todas as variantes
- [ ] `CategoryBadgeComponent` com todas as 13 categorias
- [ ] `LoadingSpinnerComponent` com mensagem parametrizável
- [ ] `SkeletonTableComponent` para loading de tabela
- [ ] `EmptyStateComponent` reutilizável
- [ ] `ErrorBannerComponent` para erros de página

### Formulário

- [ ] Campo de texto com validação em tempo real
- [ ] Contador de caracteres com estados de alerta
- [ ] Dica de preenchimento visível
- [ ] Botão com estado de loading
- [ ] Guard de mudanças não salvas

### Tabela

- [ ] Colunas: Data, Categoria, Severidade, Descrição, Ação
- [ ] Ordenação por Data e Severidade
- [ ] Paginação (20 por página default)
- [ ] Skeleton loading
- [ ] Empty state
- [ ] Destaque visual para incidentes HIGH/CRITICAL

### Feedback

- [ ] Snackbar com 4 variantes (success/error/warning/info)
- [ ] Error banner para erros de página
- [ ] Loading overlay para processamento de IA
- [ ] Mensagens de validação inline

### Acessibilidade

- [ ] Skip link implementado
- [ ] Todos os ícones com `aria-label` ou texto adjacente
- [ ] Região `role="alert"` para notificações
- [ ] Contraste verificado para todos os estados
- [ ] Navegação completa por teclado testada
- [ ] Teste com leitor de tela (NVDA/VoiceOver)

### Responsividade

- [ ] Layout testado em 320px, 768px, 1024px, 1440px
- [ ] Sidebar responsiva (drawer em mobile)
- [ ] Tabela com colunas adaptadas por breakpoint
- [ ] Touch targets ≥ 48px

---

## Apêndice B — Tokens CSS Consolidados

```scss
// Copiar para src/styles/00-settings/_tokens.scss
:root {
  // Cores primárias
  --color-primary-50:  #e3f2fd;
  --color-primary-100: #bbdefb;
  --color-primary-400: #42a5f5;
  --color-primary-600: #1976d2;
  --color-primary-700: #1565c0;
  --color-primary-900: #0d47a1;

  // Neutros
  --color-neutral-50:  #fafafa;
  --color-neutral-100: #f5f5f5;
  --color-neutral-300: #e0e0e0;
  --color-neutral-500: #9e9e9e;
  --color-neutral-700: #424242;
  --color-neutral-900: #212121;
  --color-white: #ffffff;

  // Severidade
  --severity-low-main:      #2e7d32;
  --severity-low-bg:        #e8f5e9;
  --severity-low-text:      #1b5e20;
  --severity-medium-main:   #f57f17;
  --severity-medium-bg:     #fff8e1;
  --severity-medium-text:   #e65100;
  --severity-high-main:     #e64a19;
  --severity-high-bg:       #fbe9e7;
  --severity-high-text:     #bf360c;
  --severity-critical-main: #b71c1c;
  --severity-critical-bg:   #ffebee;
  --severity-critical-text: #7f0000;

  // Espaçamentos
  --space-2:  4px;
  --space-4:  8px;
  --space-6:  12px;
  --space-8:  16px;
  --space-12: 24px;
  --space-16: 32px;
  --space-24: 48px;
  --space-32: 64px;

  // Layout
  --sidebar-width:    240px;
  --header-height:    64px;
  --content-max-width: 1200px;
  --content-padding:  var(--space-12);
}
```

---

## Apêndice C — Não Fazer

| Prática Proibida | Motivo |
|-----------------|--------|
| Usar `any` em TypeScript para dados de UI | Perde type-safety em pipes e templates |
| Inline styles no HTML (`style="..."`) | Impossível manter, quebre dark mode |
| Importar `BrowserModule` em componentes standalone | Causa erro; usar `CommonModule` |
| Usar cores fixas fora dos tokens | Quebra dark mode e consistência |
| Exibir mensagens de erro técnicas ao usuário | "HTTP 502" não orienta operadores |
| Botão de submit sem `type="submit"` ou `type="button"` | Comportamento imprevisível em formulários |
| Tabela sem `caption` ou `aria-label` | Inacessível para leitores de tela |
| Loading sem mensagem textual | Apenas spinner é insuficiente para acessibilidade |
| Dois `mat-raised-button primary` no mesmo grupo de ações | Quebra hierarquia visual |
| `text-transform: uppercase` em botões | Dificulta leitura de textos técnicos |
| Animações com `prefers-reduced-motion: no-preference` não verificado | Causa problemas de acessibilidade |
| Cores de categoria improvisadas fora do mapeamento | Inconsistência que confunde operadores |

---

**Versão:** 1.0  
**Aprovado por:** Arquitetura Frontend, Design System  
**Próxima revisão:** Agosto de 2026  
**Classificação:** Interno — Uso restrito à equipe de desenvolvimento
