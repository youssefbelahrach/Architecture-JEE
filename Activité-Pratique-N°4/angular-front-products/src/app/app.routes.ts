import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { NewProductComponent } from './components/new-product/new-product.component';
import { EditProductComponent } from './components/edit-product/edit-product.component';

export const routes: Routes = [
  { path: '', redirectTo: 'products', pathMatch: 'full' },
  { path: 'products', component: ProductListComponent },
  { path: 'new-product', component: NewProductComponent },
  { path: 'edit-product/:id', component: EditProductComponent },
  { path: '**', redirectTo: 'products' }
];
