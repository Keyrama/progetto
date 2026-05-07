import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductCardComponent } from './components/product-card/product-card.component';
import { ProductFilterComponent } from './components/product-filter/product-filter.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { ProductAlternativesComponent } from './components/product-alternatives/product-alternatives.component';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { AdminCatalogueComponent } from './components/admin-catalogue/admin-catalogue.component';
import { AdminCategoriesComponent } from './components/admin-categories/admin-categories.component';
import { AdminIngredientsComponent } from './components/admin-ingredients/admin-ingredients.component';
import { AdminClaimDefinitionsComponent } from './components/admin-claim-definitions/admin-claim-definitions.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    ProductListComponent,
    ProductCardComponent,
    ProductFilterComponent,
    ProductDetailComponent,
    ProductAlternativesComponent,
    ProductFormComponent,
    AdminCatalogueComponent,
    AdminCategoriesComponent,
    AdminIngredientsComponent,
    AdminClaimDefinitionsComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    DatePipe,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
