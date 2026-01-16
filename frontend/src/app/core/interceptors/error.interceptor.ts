import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        const refreshToken = authService.getRefreshToken();
        if(refreshToken && !req.url.includes('/auth/refresh')) {
          return authService.refresh(refreshToken).pipe(
            switchMap((response) => {
              if(response.success && response.data) {
                const clonedReq = req.clone({
                  setHeaders: {
                    Authorization: `Bearer ${response.data.tokenPair.accessToken}`
                  }
                });
                return next(clonedReq);
              }
              authService.logout();
              router.navigate(["/login"]);
              return throwError(() => error);
            }),
            catchError((refreshError) => {
              authService.logout();
              router.navigate(['/login']);
              return throwError(() => refreshError);
            })
          );
        }
        authService.logout();
        router.navigate(['/login']);
      } else if (error.status === 403) {
        router.navigate(['/unauthorized']);
      } else if (error.status === 0 || error.status === 504 || error.status === 408) {
        // Network error or timeout - don't redirect, just let component handle it
        console.error('Network error or timeout:', error);
      }
      return throwError(() => error);
    })
  );
};

