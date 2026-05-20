import { createReducer, on } from '@ngrx/store';
import { initialUiState } from './ui.state';
import { uiActions } from './ui.actions';

export const uiReducer = createReducer(
  initialUiState,
  on(uiActions.setLoading, (state, { isLoading }) => ({ ...state, isLoading }))
);
