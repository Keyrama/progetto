import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductCategoryDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { CategoryCriteria } from '../../services/filters/category-criteria';

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
})
export class AdminCategoriesComponent implements OnInit {
  readonly Math = Math;

  categories: ProductCategoryDTO[] = [];
  loading = true;
  saving = false;

  showForm = false;
  editing: ProductCategoryDTO | null = null;
  form!: FormGroup;

  toastMsg = '';
  toastError = false;

  criteria = new CategoryCriteria(0, 10);
  totalCategories = 0;

  constructor(private fb: FormBuilder, private productService: ProductService) {}

  ngOnInit() { this.loadCount(); }

  loadCount() {
    this.productService.getCategoriesCount().subscribe(count => {
      this.totalCategories = count;
      this.load();
    });
  }

  load() {
    this.loading = true;
    this.productService.getCategories(this.criteria).subscribe(cats => {
      this.categories = cats;
      this.loading = false;
    });
  }

  onPageChange(event: any) {
    this.criteria.offset = event.first;
    this.criteria.limit = event.rows;
    this.load();
  }

  openCreate() {
    this.editing = null;
    this.form = this.fb.group({
      name:        ['', Validators.required],
      description: [''],
    });
    this.showForm = true;
  }

  openEdit(cat: ProductCategoryDTO) {
    this.editing = cat;
    this.form = this.fb.group({
      name:        [cat.name, Validators.required],
      description: [cat.description ?? ''],
    });
    this.showForm = true;
  }

  closeForm() { this.showForm = false; this.editing = null; }

  onSubmit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;

    const payload: ProductCategoryDTO = {
      ...(this.editing ? { id: this.editing.id } : {}),
      ...this.form.value,
    };

    const op = this.editing
      ? this.productService.updateCategory(payload, this.editing.id)
      : this.productService.createCategory(payload);

    op.subscribe({
      next: () => {
        this.saving = false;
        this.closeForm();
        this.criteria.offset = 0;
        this.loadCount();
        this.showToast(this.editing ? 'Categoria aggiornata.' : 'Categoria creata.');
      },
      error: () => {
        this.saving = false;
        this.showToast('Errore durante il salvataggio.', true);
      },
    });
  }

  deleteCategory(cat: ProductCategoryDTO) {
    if (!cat.id) return;
    if (!confirm(`Eliminare la categoria "${cat.name}"?`)) return;
    this.productService.deleteCategory(cat.id).subscribe({
      next: () => {
        this.criteria.offset = 0;
        this.loadCount();
        this.showToast('Categoria eliminata.');
      },
      error: () => this.showToast('Errore durante l\'eliminazione.', true),
    });
  }

  showToast(msg: string, error = false) {
    this.toastMsg = msg;
    this.toastError = error;
    setTimeout(() => this.toastMsg = '', 3000);
  }

  get f() { return this.form?.controls ?? {}; }
}
