import { Routes } from '@angular/router';
export const COURIERS_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./pages/courier-list/courier-list.component').then(m => m.CourierListComponent) }
];
