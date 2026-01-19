import { Routes } from '@angular/router';

export const PRODUCTS_ROUTES: Routes = [
  { path: 'my-products', loadComponent: () => import('./pages/my-products/my-products.component').then(m => m.MyProductsComponent) }
];

