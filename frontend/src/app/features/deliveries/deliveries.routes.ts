import { Routes } from '@angular/router';
export const DELIVERIES_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./pages/delivery-list/delivery-list.component').then(m => m.DeliveryListComponent) },
  { path: ':id', loadComponent: () => import('./pages/delivery-detail/delivery-detail.component').then(m => m.DeliveryDetailComponent) }
];
