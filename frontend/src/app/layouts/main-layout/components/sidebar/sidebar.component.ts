import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent {
  authService = inject(AuthService);

  logout() {
    this.authService.logout();
  }

  isSender(): boolean {
    const user = this.authService.getStoredUser();
    return user?.role === 'ROLE_SENDER';
  }

  isManager(): boolean {
    const user = this.authService.getStoredUser();
    return user?.role === 'ROLE_MANAGER' || user?.role === 'ROLE_ADMIN';
  }

  isCourier(): boolean {
    const user = this.authService.getStoredUser();
    return user?.role === 'ROLE_COURIER';
  }
}