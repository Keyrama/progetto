import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { AdminCatalogueComponent } from './components/admin-catalogue/admin-catalogue.component';

const routes: Routes = [
  { path: '',             redirectTo: 'products', pathMatch: 'full' },
  { path: 'products',     component: ProductListComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: 'admin',        component: AdminCatalogueComponent },
  { path: '**',           redirectTo: 'products' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
