import { Routes } from '@angular/router';

export const TRACKING_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/tracking/tracking.component').then(m => m.TrackingComponent)
  }
];

