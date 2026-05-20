import { createActionGroup, props } from '@ngrx/store';

export const uiActions = createActionGroup({
  source: 'UI',
  events: {
    'Set Loading': props<{ isLoading: boolean }>()
  }
});
