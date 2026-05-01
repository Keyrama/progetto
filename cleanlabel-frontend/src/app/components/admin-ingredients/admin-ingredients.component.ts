import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IngredientDTO, AllergenDTO, RiskLevel } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-admin-ingredients',
  templateUrl: './admin-ingredients.component.html',
})
export class AdminIngredientsComponent implements OnInit {
  ingredients: IngredientDTO[] = [];
  allergens: AllergenDTO[] = [];
  loading = true;
  saving = false;

  showForm = false;
  editing: IngredientDTO | null = null;
  form!: FormGroup;

  selectedAllergenIds = new Set<number>();

  // Delete confirmation modal state
  ingredientToDelete: IngredientDTO | null = null;

  readonly riskLevels: RiskLevel[] = ['LOW', 'MEDIUM', 'HIGH'];

  toastMsg = '';
  toastError = false;

  filterText = '';

  constructor(private fb: FormBuilder, private productService: ProductService) {}

  ngOnInit() {
    this.load();
    this.productService.getAllergens().subscribe(a => this.allergens = a);
  }

  load() {
    this.loading = true;
    this.productService.getIngredients().subscribe(ings => {
      this.ingredients = ings;
      this.loading = false;
    });
  }

  get filtered(): IngredientDTO[] {
    const q = this.filterText.toLowerCase();
    if (!q) return this.ingredients;
    return this.ingredients.filter(i =>
      i.name.toLowerCase().includes(q) ||
      (i.additiveCode ?? '').toLowerCase().includes(q)
    );
  }

  openCreate() {
    this.editing = null;
    this.selectedAllergenIds = new Set();
    this.form = this.buildForm(null);
    this.showForm = true;
  }

  openEdit(ing: IngredientDTO) {
    this.editing = ing;
    this.selectedAllergenIds = new Set(ing.allergens?.map(a => a.id) ?? []);
    this.form = this.buildForm(ing);
    this.showForm = true;
  }

  closeForm() {
    this.showForm = false;
    this.editing = null;
  }

  private buildForm(ing: IngredientDTO | null): FormGroup {
    return this.fb.group({
      name:         [ing?.name        ?? '', Validators.required],
      additiveCode: [ing?.additiveCode ?? ''],
      description:  [ing?.description ?? ''],
      artificial:   [ing?.artificial  ?? false],
      riskLevel:    [ing?.riskLevel   ?? 'LOW', Validators.required],
    });
  }

  toggleAllergen(id: number) {
    this.selectedAllergenIds.has(id)
      ? this.selectedAllergenIds.delete(id)
      : this.selectedAllergenIds.add(id);
  }

  isAllergenSelected(id: number): boolean {
    return this.selectedAllergenIds.has(id);
  }

  onSubmit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;

    const payload: IngredientDTO = {
      ...this.form.value,
      allergens: this.allergens
        .filter(a => this.selectedAllergenIds.has(a.id))
        .map(a => ({ id: a.id, name: a.name, code: a.code })),
    };

    const op = this.editing?.id
      ? this.productService.updateIngredient(this.editing.id, payload)
      : this.productService.createIngredient(payload);

    op.subscribe({
      next: () => {
        this.saving = false;
        this.closeForm();
        this.load();
        this.showToast(this.editing ? 'Ingrediente aggiornato.' : 'Ingrediente creato.');
      },
      error: () => {
        this.saving = false;
        this.showToast('Errore durante il salvataggio.', true);
      },
    });
  }

  // ── Delete with modal confirmation ────────────────────────────────────────

  confirmDelete(ing: IngredientDTO) {
    this.ingredientToDelete = ing;
  }

  cancelDelete() {
    this.ingredientToDelete = null;
  }

  doDelete() {
    if (!this.ingredientToDelete?.id) return;
    this.productService.deleteIngredient(this.ingredientToDelete.id).subscribe({
      next: () => {
        this.ingredientToDelete = null;
        this.load();
        this.showToast('Ingrediente eliminato.');
      },
      error: () => {
        this.ingredientToDelete = null;
        this.showToast('Errore durante l\'eliminazione.', true);
      },
    });
  }

  riskBadgeClass(level: RiskLevel): string {
    if (level === 'HIGH')   return 'bg-danger';
    if (level === 'MEDIUM') return 'bg-warning text-dark';
    return 'bg-success';
  }

  showToast(msg: string, error = false) {
    this.toastMsg = msg;
    this.toastError = error;
    setTimeout(() => this.toastMsg = '', 3000);
  }

  get f() { return this.form?.controls ?? {}; }
}
