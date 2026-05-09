import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ProductCategoryDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { CategoryCriteria } from '../../services/filters/category-criteria';

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
})
export class AdminCategoriesComponent implements OnInit {
  readonly Math = Math;

  @Output() createRequested = new EventEmitter<void>();
  @Output() editRequested   = new EventEmitter<ProductCategoryDTO>();
  @Output() deleteRequested = new EventEmitter<ProductCategoryDTO>();
  @Output() deleted         = new EventEmitter<ProductCategoryDTO>();
  @Output() deleteFailed    = new EventEmitter<ProductCategoryDTO>();

  categories: ProductCategoryDTO[] = [];
  loading = true;

  criteria = new CategoryCriteria(0, 10);
  totalCategories = 0;

  constructor(private productService: ProductService) {}

  ngOnInit() { this.loadCount(); }

  loadCount() {
    this.productService.getCategoriesCount(this.criteria).subscribe(count => {
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

  doDelete(cat: ProductCategoryDTO) {
    if (!cat.id) return;
    this.productService.deleteCategory(cat.id).subscribe({
      next: () => {
        this.criteria.offset = 0;
        this.loadCount();
        this.deleted.emit(cat);
      },
      error: () => {
        this.deleteFailed.emit(cat);
      },
    });
  }
}
