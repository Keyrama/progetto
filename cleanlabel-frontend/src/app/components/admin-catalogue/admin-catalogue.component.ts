import { Component, OnInit } from '@angular/core';
import { ProductSummary, ProductCategory } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-admin-catalogue',
  templateUrl: './admin-catalogue.component.html',
  styleUrls: ['./admin-catalogue.component.scss'],
})
export class AdminCatalogueComponent implements OnInit {
  products: ProductSummary[] = [];
  categories: ProductCategory[] = [];
  loading = true;
  showForm = false;
  selectedProduct: ProductSummary | null = null;
  productToDelete: ProductSummary | null = null;
  toastMsg = '';

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.load();
    this.productService.getCategories().subscribe(c => this.categories = c);
  }

  load() {
    this.loading = true;
    this.productService.getProducts({ size: 100 }).subscribe(page => {
      this.products = page.content;
      this.loading = false;
    });
  }

  openCreate() { this.selectedProduct = null; this.showForm = true; }
  openEdit(p: ProductSummary) { this.selectedProduct = p; this.showForm = true; }
  closeForm() { this.showForm = false; this.selectedProduct = null; }

  onSaved(product: any) {
    this.closeForm();
    this.load();
    this.showToast(this.selectedProduct ? 'Prodotto aggiornato!' : 'Prodotto creato!');
  }

  confirmDelete(p: ProductSummary) { this.productToDelete = p; }

  doDelete() {
    if (!this.productToDelete) return;
    this.productService.deleteProduct(this.productToDelete.id).subscribe(() => {
      this.productToDelete = null;
      this.load();
      this.showToast('Prodotto eliminato.');
    });
  }

  showToast(msg: string) {
    this.toastMsg = msg;
    setTimeout(() => this.toastMsg = '', 3000);
  }

  scoreClass(score: number): string {
    if (score >= 70) return 'score-high';
    if (score >= 40) return 'score-medium';
    return 'score-low';
  }
}
