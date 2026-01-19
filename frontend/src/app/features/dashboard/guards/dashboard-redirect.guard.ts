import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

export const dashboardRedirectGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  const user = authService.getStoredUser();
  
  if (user?.role === 'ROLE_SENDER') {
    router.navigate(['/dashboard/sender'], { replaceUrl: true });
  } else {
    router.navigate(['/dashboard/manager'], { replaceUrl: true });
  }
  
  return false; // Prevent route activation since we're redirecting
};

