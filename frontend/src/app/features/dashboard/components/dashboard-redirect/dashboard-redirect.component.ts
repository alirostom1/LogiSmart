import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-dashboard-redirect',
  standalone: true,
  template: '<div></div>' // Empty template since we just redirect
})
export class DashboardRedirectComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  ngOnInit() {
    const user = this.authService.getStoredUser();
    
    if (user?.role === 'ROLE_SENDER') {
      this.router.navigate(['/dashboard/sender'], { replaceUrl: true });
    } else if (user?.role === 'ROLE_COURIER') {
      this.router.navigate(['/dashboard/courier'], { replaceUrl: true });
    } else {
      this.router.navigate(['/dashboard/manager'], { replaceUrl: true });
    }
  }
}

