import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'track',
    loadChildren: () => import('./features/tracking/tracking.routes').then(m => m.TRACKING_ROUTES)
  },
  {
    path: '',
    loadComponent: () => import('./layouts/main-layout/main-layout.component').then(m => m.MainLayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES)
      },
      {
        path: 'deliveries',
        loadChildren: () => import('./features/deliveries/deliveries.routes').then(m => m.DELIVERIES_ROUTES)
      },
      {
        path: 'clients',
        loadChildren: () => import('./features/clients/clients.routes').then(m => m.CLIENTS_ROUTES)
      },
      {
        path: 'couriers',
        loadChildren: () => import('./features/couriers/couriers.routes').then(m => m.COURIERS_ROUTES)
      },
      {
        path: 'zones',
        loadChildren: () => import('./features/zones/zones.routes').then(m => m.ZONES_ROUTES)
      },
      {
        path: 'products',
        loadChildren: () => import('./features/products/products.routes').then(m => m.PRODUCTS_ROUTES)
      }
    ]
  },
  { path: '**', redirectTo: 'auth/login' }
];