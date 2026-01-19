import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isAuthenticated = authService.isAuthenticated();
  const token = authService.getAccessToken();
  const user = authService.getStoredUser();

  console.log('AuthGuard check:', {
    isAuthenticated,
    hasToken: !!token,
    hasUser: !!user,
    route: state.url
  });

  if (isAuthenticated) {
    return true;
  }

  console.log('Not authenticated, redirecting to login');
  router.navigate(['/auth/login']);
  return false;
};

