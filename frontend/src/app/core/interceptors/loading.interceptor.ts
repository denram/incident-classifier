import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { finalize } from 'rxjs/operators';
import { uiActions } from '../store/ui/ui.actions';

export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);

  store.dispatch(uiActions.setLoading({ isLoading: true }));

  return next(req).pipe(
    finalize(() => store.dispatch(uiActions.setLoading({ isLoading: false })))
  );
};
