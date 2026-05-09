import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription, skip } from 'rxjs';
import { ProductDTO, ProductCategoryDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ProductCriteria } from '../../services/filters/product-criteria';
import { AdminClaimDefinitionsComponent } from '../admin-claim-definitions/admin-claim-definitions.component';
import { AdminCategoriesComponent } from '../admin-categories/admin-categories.component';

@Component({
  selector: 'app-admin-catalogue',
  templateUrl: './admin-catalogue.component.html',
  styleUrls: ['./admin-catalogue.component.scss'],
})
export class AdminCatalogueComponent implements OnInit, OnDestroy {

  @ViewChild(AdminClaimDefinitionsComponent)
  private claimDefinitionsRef?: AdminClaimDefinitionsComponent;

  @ViewChild(AdminCategoriesComponent)
  private categoriesRef?: AdminCategoriesComponent;

  readonly Math = Math;

  activeTab: 'products' | 'categories' | 'ingredients' | 'claims' = 'products';

  products: ProductDTO[] = [];
  categories: ProductCategoryDTO[] = [];
  loading = true;
  showForm = false;
  selectedProduct: ProductDTO | null = null;
  productToDelete: ProductDTO | null = null;
  toastMsg = '';
  toastError = false;

  criteria = new ProductCriteria(0, 5);
  totalProducts = 0;

  // Category form
  showCategoryForm = false;
  editingCategory: ProductCategoryDTO | null = null;
  categoryForm!: FormGroup;
  savingCategory = false;
  categoryToDelete: ProductCategoryDTO | null = null;

  private userSub!: Subscription;

  get isCorporate(): boolean { return this.auth.hasRole('CORPORATE'); }
  get isSpecialist(): boolean { return this.auth.hasRole('SPECIALIST'); }

  constructor(
    private productService: ProductService,
    public auth: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    if (!this.auth.hasRole('SPECIALIST', 'CORPORATE')) {
      this.router.navigate(['/products']);
      return;
    }

    this.activeTab = this.isCorporate ? 'products' : 'claims';

    if (this.isCorporate) {
      this.loadCount();
      this.reloadCategories();
    }

    this.userSub = this.auth.currentUser$.pipe(skip(1)).subscribe(user => {
      if (!user || (user.role !== 'CORPORATE' && user.role !== 'SPECIALIST')) {
        this.router.navigate(['/products']);
        return;
      }

      if (user.role === 'SPECIALIST' && this.activeTab !== 'claims') {
        this.activeTab = 'claims';
        setTimeout(() => this.claimDefinitionsRef?.loadCount());
      } else if (user.role === 'CORPORATE' && this.activeTab === 'claims') {
        this.activeTab = 'products';
        this.loadCount();
        this.reloadCategories();
      }
    });
  }

  ngOnDestroy() { this.userSub?.unsubscribe(); }

  // ── Products ──────────────────────────────────────────────────────────────

  loadCount() {
    this.productService.getProductsCount(this.criteria).subscribe(count => {
      this.totalProducts = count;
      this.load();
    });
  }

  load() {
    this.loading = true;
    this.productService.getProducts(this.criteria).subscribe(products => {
      this.products = products;
      this.loading = false;
    });
  }

  onProductPageChange(event: any) {
    this.criteria.offset = event.first;
    this.criteria.limit = event.rows;
    this.load();
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
    this.loadCount();
    this.showToast('Prodotto salvato con successo.');
  }

  onSaveError() { this.showToast('Errore durante il salvataggio.', true); }

  confirmDelete(p: ProductDTO) { this.productToDelete = p; }

  doDelete() {
    if (!this.productToDelete?.id) return;
    this.productService.deleteProduct(this.productToDelete.id).subscribe({
      next: () => {
        this.productToDelete = null;
        this.criteria.offset = 0;
        this.loadCount();
        this.showToast('Prodotto eliminato.');
      },
      error: () => this.showToast('Errore durante l\'eliminazione.', true)
    });
  }

  // ── Categories ────────────────────────────────────────────────────────────

  openCategoryCreate() {
    this.editingCategory = null;
    this.categoryForm = this.fb.group({
      name:        ['', Validators.required],
      description: [''],
    });
    this.showCategoryForm = true;
  }

  openCategoryEdit(cat: ProductCategoryDTO) {
    this.editingCategory = cat;
    this.categoryForm = this.fb.group({
      name:        [cat.name, Validators.required],
      description: [cat.description ?? ''],
    });
    this.showCategoryForm = true;
  }

  closeCategoryForm() { this.showCategoryForm = false; this.editingCategory = null; }

  onCategorySubmit() {
    if (this.categoryForm.invalid) { this.categoryForm.markAllAsTouched(); return; }
    this.savingCategory = true;

    const wasEditing = !!this.editingCategory;
    const payload: ProductCategoryDTO = {
      ...(this.editingCategory ? { id: this.editingCategory.id } : {}),
      ...this.categoryForm.value,
    };

    const op = this.editingCategory
      ? this.productService.updateCategory(payload, this.editingCategory.id)
      : this.productService.createCategory(payload);

    op.subscribe({
      next: () => {
        this.savingCategory = false;
        this.closeCategoryForm();
        this.categoriesRef?.loadCount();
        this.showToast(wasEditing ? 'Categoria aggiornata.' : 'Categoria creata.');
      },
      error: () => {
        this.savingCategory = false;
        this.showToast('Errore durante il salvataggio.', true);
      },
    });
  }

  confirmCategoryDelete(cat: ProductCategoryDTO) { this.categoryToDelete = cat; }

  doCategoryDelete() {
    if (!this.categoryToDelete) return;
    this.categoriesRef?.doDelete(this.categoryToDelete);
    this.categoryToDelete = null;
  }

  onCategoryDeleted(cat: ProductCategoryDTO) {
    this.showToast(`Categoria "${cat.name}" eliminata.`);
  }

  onCategoryDeleteFailed(cat: ProductCategoryDTO) {
    this.showToast(`Impossibile eliminare "${cat.name}": ci sono prodotti collegati a questa categoria.`, true);
  }

  // ─────────────────────────────────────────────────────────────────────────

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
