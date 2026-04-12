import { Component, OnInit } from '@angular/core';
import { ProductDTO, ProductCategoryDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-catalogue',
  templateUrl: './admin-catalogue.component.html',
  styleUrls: ['./admin-catalogue.component.scss'],
})
export class AdminCatalogueComponent implements OnInit {
  products: ProductDTO[] = [];
  categories: ProductCategoryDTO[] = [];
  loading = true;
  showForm = false;
  selectedProduct: ProductDTO | null = null;
  productToDelete: ProductDTO | null = null;
  toastMsg = '';
  toastError = false;

  constructor(
    private productService: ProductService,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    // Guard: redirect non-CORPORATE users
    if (!this.auth.hasRole('CORPORATE')) {
      this.router.navigate(['/products']);
      return;
    }
    this.load();
    this.productService.getCategories().subscribe(c => this.categories = c);
  }

  load() {
    this.loading = true;
    this.productService.getProducts({}).subscribe(products => {
      this.products = products;
      this.loading = false;
    });
  }

  openCreate() { this.selectedProduct = null; this.showForm = true; }
  openEdit(p: ProductDTO) { this.selectedProduct = p; this.showForm = true; }
  closeForm() { this.showForm = false; this.selectedProduct = null; }

  onSaved() {
    this.closeForm();
    this.load();
    this.showToast('Prodotto salvato con successo.');
  }

  onSaveError() {
    this.showToast('Errore durante il salvataggio. Verifica i dati.', true);
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
