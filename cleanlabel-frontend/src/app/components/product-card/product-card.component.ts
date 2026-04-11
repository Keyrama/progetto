import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ProductSummary } from '../../models/product.model';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
})
export class ProductCardComponent {
  @Input() product!: ProductSummary;
  @Output() selected = new EventEmitter<number>();

  get scoreClass(): string {
    if (this.product.healthScore >= 70) return 'score-high';
    if (this.product.healthScore >= 40) return 'score-medium';
    return 'score-low';
  }

  onClick() { this.selected.emit(this.product.id); }
}
