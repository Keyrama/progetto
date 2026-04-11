import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { ProductCategory, ProductFilter } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-filter',
  templateUrl: './product-filter.component.html',
})
export class ProductFilterComponent implements OnInit {
  @Input()  filter: ProductFilter = {};
  @Output() filterChange = new EventEmitter<ProductFilter>();

  categories: ProductCategory[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.getCategories().subscribe(cats => this.categories = cats);
  }

  onSearchChange(val: string) {
    this.filterChange.emit({ ...this.filter, search: val || undefined, page: 0 });
  }
  onCategoryChange(id: number | undefined) {
    this.filterChange.emit({ ...this.filter, categoryId: id, page: 0 });
  }
  onCleanLabelChange(val: boolean) {
    this.filterChange.emit({ ...this.filter, cleanLabelOnly: val || undefined, page: 0 });
  }
  onMinScoreChange(val: number) {
    this.filterChange.emit({ ...this.filter, minScore: val || undefined, page: 0 });
  }
  onReset() {
    this.filterChange.emit({ page: 0, size: 12 });
  }
}
