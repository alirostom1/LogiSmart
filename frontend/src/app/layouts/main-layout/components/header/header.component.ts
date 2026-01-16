import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  authService = inject(AuthService);
  router = inject(Router);
  
  currentRoute = '';

  ngOnInit() {
    this.currentRoute = this.router.url;
    
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.currentRoute = event.url;
    });
  }

  get user() {
    return this.authService.getStoredUser();
  }

  get roleLabel() {
    const role = this.user?.role;
    if (!role) return '';
    const labels: Record<string, string> = {
      'ROLE_MANAGER': 'Gestionnaire',
      'ROLE_ADMIN': 'Administrateur',
      'ROLE_SENDER': 'Expéditeur',
      'ROLE_COURIER': 'Livreur'
    };
    return labels[role] || role;
  }

  getPageTitle(): string {
    const titles: Record<string, string> = {
      '/dashboard': 'Tableau de bord',
      '/deliveries': 'Gestion des Colis',
      '/clients': 'Gestion des Clients',
      '/couriers': 'Gestion des Livreurs',
      '/zones': 'Gestion des Zones'
    };
    
    // Find matching route
    for (const [route, title] of Object.entries(titles)) {
      if (this.currentRoute.startsWith(route)) {
        return title;
      }
    }
    
    return 'LogiSmart';
  }
}

