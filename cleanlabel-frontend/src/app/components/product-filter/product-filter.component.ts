import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProductCategoryDTO, ProductFilter } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-filter',
  templateUrl: './product-filter.component.html',
})
export class ProductFilterComponent implements OnInit {
  @Input() filter: ProductFilter = {};
  @Output() filterChange = new EventEmitter<ProductFilter>();
  @Output() reset = new EventEmitter<void>();

  categories: ProductCategoryDTO[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.getCategories().subscribe(c => this.categories = c);
  }

  onSearchChange(value: string) {
    this.filter = { ...this.filter, search: value || undefined };
    this.filterChange.emit(this.filter);
  }

  onCategoryChange(id: number | undefined) {
    this.filter = { ...this.filter, category: id };
    this.filterChange.emit(this.filter);
  }

  onCleanLabelChange(value: boolean) {
    this.filter = { ...this.filter, cleanLabel: value || undefined };
    this.filterChange.emit(this.filter);
  }

  onReset() {
    this.filter = {};
    this.reset.emit();
  }
}
