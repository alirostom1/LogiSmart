import { Routes } from '@angular/router';

export const DASHBOARD_ROUTES: Routes = [
  { 
    path: '', 
    loadComponent: () => import('./components/dashboard-redirect/dashboard-redirect.component').then(m => m.DashboardRedirectComponent),
    pathMatch: 'full'
  },
  { 
    path: 'manager', 
    loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent) 
  },
  { 
    path: 'sender', 
    loadComponent: () => import('./pages/sender-dashboard/sender-dashboard.component').then(m => m.SenderDashboardComponent) 
  },
  { 
    path: 'courier', 
    loadComponent: () => import('./pages/courier-dashboard/courier-dashboard.component').then(m => m.CourierDashboardComponent) 
  }
];
