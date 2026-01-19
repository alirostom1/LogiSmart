import { Routes } from '@angular/router';
export const DELIVERIES_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./pages/delivery-list/delivery-list.component').then(m => m.DeliveryListComponent) },
  { path: 'new', loadComponent: () => import('./pages/create-delivery/create-delivery.component').then(m => m.CreateDeliveryComponent) },
  { path: 'my-deliveries', loadComponent: () => import('./pages/my-deliveries/my-deliveries.component').then(m => m.MyDeliveriesComponent) },
  { path: ':id', loadComponent: () => import('./pages/delivery-detail/delivery-detail.component').then(m => m.DeliveryDetailComponent) }
];
