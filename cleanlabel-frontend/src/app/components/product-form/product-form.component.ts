import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductSummary, ProductCategory } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
})
export class ProductFormComponent implements OnInit {
  @Input() product: ProductSummary | null = null;
  @Input() categories: ProductCategory[] = [];
  @Output() saved = new EventEmitter<any>();
  @Output() cancelled = new EventEmitter<void>();

  form!: FormGroup;
  saving = false;

  constructor(private fb: FormBuilder, private productService: ProductService) {}

  ngOnInit() {
    const nv = (this.product as any)?.nutritionalValue;
    this.form = this.fb.group({
      name:        [this.product?.name        ?? '', Validators.required],
      brand:       [this.product?.brand       ?? '', Validators.required],
      description: [this.product?.description ?? ''],
      categoryId:  [this.product?.category?.id ?? null],
      isCleanLabel:[this.product?.isCleanLabel ?? false],
      nutritionalValue: this.fb.group({
        calories:      [nv?.calories      ?? null],
        proteins:      [nv?.proteins      ?? null],
        carbohydrates: [nv?.carbohydrates ?? null],
        sugars:        [nv?.sugars        ?? null],
        fats:          [nv?.fats          ?? null],
        saturatedFats: [nv?.saturatedFats ?? null],
        salt:          [nv?.salt          ?? null],
        fiber:         [nv?.fiber         ?? null],
      }),
    });
  }

  get f() { return this.form.controls; }

  onSubmit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;
    const payload = this.form.value;

    const op = this.product
      ? this.productService.updateProduct(this.product.id, payload)
      : this.productService.createProduct(payload);

    op.subscribe(result => {
      this.saving = false;
      this.saved.emit(result);
    });
  }
}
