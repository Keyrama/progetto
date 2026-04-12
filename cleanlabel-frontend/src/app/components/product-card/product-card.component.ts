import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProductDTO } from '../../models/product.model';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
})
export class ProductCardComponent {
  @Input() product!: ProductDTO;
  @Output() selected = new EventEmitter<number>();

  get scoreClass(): string {
    const s = this.product.healthScore;
    if (s == null) return 'score-unknown';
    if (s >= 70) return 'score-high';
    if (s >= 40) return 'score-medium';
    return 'score-low';
  }

  onClick() {
    if (this.product.id != null) this.selected.emit(this.product.id);
  }
}
