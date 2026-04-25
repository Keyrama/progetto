import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ProductDTO, ProductCategoryDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-catalogue',
  templateUrl: './admin-catalogue.component.html',
  styleUrls: ['./admin-catalogue.component.scss'],
})
export class AdminCatalogueComponent implements OnInit, OnDestroy {
  activeTab: 'products' | 'categories' | 'ingredients' = 'products';

  products: ProductDTO[] = [];
  categories: ProductCategoryDTO[] = [];
  loading = true;
  showForm = false;
  selectedProduct: ProductDTO | null = null;
  productToDelete: ProductDTO | null = null;
  toastMsg = '';
  toastError = false;

  private userSub!: Subscription;

  constructor(
    private productService: ProductService,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    // Guard iniziale
    if (!this.auth.hasRole('CORPORATE')) {
      this.router.navigate(['/products']);
      return;
    }

    this.load();
    this.reloadCategories();

    // Reagisce in tempo reale al cambio di ruolo
    this.userSub = this.auth.currentUser$.subscribe(user => {
      const isCorporate = user?.role === 'CORPORATE';
      if (!isCorporate) {
        this.router.navigate(['/products']);
      }
    });
  }

  ngOnDestroy() {
    this.userSub?.unsubscribe();
  }

  load() {
    this.loading = true;
    this.productService.getProducts({}).subscribe(products => {
      this.products = products;
      this.loading = false;
    });
  }

  reloadCategories() {
    this.productService.getCategories().subscribe(c => this.categories = c);
  }

  openCreate() {
    this.selectedProduct = null;
    this.reloadCategories();
    this.showForm = true;
  }

  openEdit(p: ProductDTO) {
    this.productService.getProduct(p.id!).subscribe(fullProduct => {
      this.selectedProduct = {
        ...fullProduct,
        ingredientIds: fullProduct.ingredients?.map(i => i.id!) ?? [],
        mayContainAllergenIds: fullProduct.mayContainAllergens?.map(a => a.id) ?? [],
        categoryId: fullProduct.categoryId,
      };
      this.reloadCategories();
      this.showForm = true;
    });
  }

  closeForm() { this.showForm = false; this.selectedProduct = null; }

  onSaved() {
    this.closeForm();
    this.load();
    this.showToast('Prodotto salvato con successo.');
  }

  onSaveError() {
    this.showToast('Errore durante il salvataggio.', true);
  }

  confirmDelete(p: ProductDTO) { this.productToDelete = p; }

  doDelete() {
    if (!this.productToDelete?.id) return;
    this.productService.deleteProduct(this.productToDelete.id).subscribe({
      next: () => {
        this.productToDelete = null;
        this.load();
        this.showToast('Prodotto eliminato.');
      },
      error: () => this.showToast('Errore durante l\'eliminazione.', true)
    });
  }

  showToast(msg: string, error = false) {
    this.toastMsg = msg;
    this.toastError = error;
    setTimeout(() => this.toastMsg = '', 3500);
  }

  scoreClass(score: number | undefined): string {
    if (score == null) return 'text-muted';
    if (score >= 70) return 'score-high';
    if (score >= 40) return 'score-medium';
    return 'score-low';
  }
}
