import { Routes } from '@angular/router';
export const ZONES_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./pages/zone-list/zone-list.component').then(m => m.ZoneListComponent) }
];
