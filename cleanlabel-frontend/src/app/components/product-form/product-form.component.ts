import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductDTO, ProductCategoryDTO, IngredientDTO, AllergenDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
})
export class ProductFormComponent implements OnInit {
  @Input() product: ProductDTO | null = null;
  @Input() categories: ProductCategoryDTO[] = [];
  @Output() saved = new EventEmitter<void>();
  @Output() saveError = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  form!: FormGroup;
  saving = false;

  // Available ingredients and allergens for multi-select
  availableIngredients: IngredientDTO[] = [];
  availableAllergens: AllergenDTO[] = [];

  // Selected IDs (managed as sets for O(1) toggle)
  selectedIngredientIds = new Set<number>();
  selectedAllergenIds = new Set<number>();

  constructor(private fb: FormBuilder, private productService: ProductService) {}

  ngOnInit() {
    const nv = this.product?.nutritionalValue;

    this.form = this.fb.group({
      name:                [this.product?.name        ?? '', Validators.required],
      brand:               [this.product?.brand       ?? '', Validators.required],
      description:         [this.product?.description ?? ''],
      categoryId:          [this.product?.categoryId  ?? null],
      sustainabilityScore: [this.product?.sustainabilityScore ?? null],
      nutritionalValue: this.fb.group({
        calories:      [nv?.calories      ?? null, Validators.min(0)],
        proteins:      [nv?.proteins      ?? null, Validators.min(0)],
        carbohydrates: [nv?.carbohydrates ?? null, Validators.min(0)],
        sugars:        [nv?.sugars        ?? null, Validators.min(0)],
        fats:          [nv?.fats          ?? null, Validators.min(0)],
        saturatedFats: [nv?.saturatedFats ?? null, Validators.min(0)],
        salt:          [nv?.salt          ?? null, Validators.min(0)],
        fiber:         [nv?.fiber         ?? null, Validators.min(0)],
      }),
    });

    // Pre-populate selected IDs from the product being edited
    if (this.product?.ingredientIds) {
      this.product.ingredientIds.forEach(id => this.selectedIngredientIds.add(id));
    }
    if (this.product?.mayContainAllergenIds) {
      this.product.mayContainAllergenIds.forEach(id => this.selectedAllergenIds.add(id));
    }

    // Load available options
    this.productService.getIngredients().subscribe(i => this.availableIngredients = i);
    this.productService.getAllergens().subscribe(a => this.availableAllergens = a);
  }

  get f() { return this.form.controls; }

  toggleIngredient(id: number) {
    this.selectedIngredientIds.has(id)
      ? this.selectedIngredientIds.delete(id)
      : this.selectedIngredientIds.add(id);
  }

  toggleAllergen(id: number) {
    this.selectedAllergenIds.has(id)
      ? this.selectedAllergenIds.delete(id)
      : this.selectedAllergenIds.add(id);
  }

  isIngredientSelected(id: number): boolean { return this.selectedIngredientIds.has(id); }
  isAllergenSelected(id: number): boolean   { return this.selectedAllergenIds.has(id); }

  riskClass(level: string): string {
    if (level === 'HIGH')   return 'text-danger';
    if (level === 'MEDIUM') return 'text-warning';
    return 'text-success';
  }

  onSubmit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;

    const payload: ProductDTO = {
      ...this.form.value,
      ingredientIds:       Array.from(this.selectedIngredientIds),
      mayContainAllergenIds: Array.from(this.selectedAllergenIds),
    };

    const op = this.product?.id
      ? this.productService.updateProduct(this.product.id, payload)
      : this.productService.createProduct(payload);

    op.subscribe({
      next: () => { this.saving = false; this.saved.emit(); },
      error: () => { this.saving = false; this.saveError.emit(); },
    });
  }
}
