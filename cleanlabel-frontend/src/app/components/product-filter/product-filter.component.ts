import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProductCategoryDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { ProductCriteria } from '../../services/filters/product-criteria';

@Component({
  selector: 'app-product-filter',
  templateUrl: './product-filter.component.html',
})
export class ProductFilterComponent implements OnInit {
  @Input() criteria: ProductCriteria = new ProductCriteria();
  @Output() criteriaChange = new EventEmitter<Partial<Pick<ProductCriteria, 'search' | 'category' | 'cleanLabel'>>>();
  @Output() reset = new EventEmitter<void>();

  categories: ProductCategoryDTO[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.productService.getCategories().subscribe(c => this.categories = c);
  }

  onSearchChange(value: string) {
    this.criteriaChange.emit({ search: value || undefined });
  }

  onCategoryChange(id: number | undefined) {
    this.criteriaChange.emit({ category: id });
  }

  onCleanLabelChange(value: boolean) {
    this.criteriaChange.emit({ cleanLabel: value || undefined });
  }

  onReset() {
    this.reset.emit();
  }
}
