import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { environment } from '../../../../environment';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  if (token && !req.url.includes('/auth/login') && !req.url.includes('/auth/register')) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `${environment.token.tokenType} ${token}`
      }
    });
    return next(clonedReq);
  }

  return next(req);
};

