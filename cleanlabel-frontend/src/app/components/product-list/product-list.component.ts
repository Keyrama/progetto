import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs/operators';
import { ProductSummary, ProductFilter } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: ProductSummary[] = [];
  filter: ProductFilter = { page: 0, size: 12 };
  loading = false;
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;
  pageSize = 12;

  private filter$ = new Subject<ProductFilter>();
  private destroy$ = new Subject<void>();

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.filter$.pipe(
      debounceTime(300),
      distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
      switchMap(f => { this.loading = true; return this.productService.getProducts(f); }),
      takeUntil(this.destroy$)
    ).subscribe(page => {
      this.products = page.content;
      this.totalElements = page.totalElements;
      this.totalPages = page.totalPages;
      this.currentPage = page.number;
      this.loading = false;
    });

    this.filter$.next(this.filter);
  }

  ngOnDestroy() { this.destroy$.next(); this.destroy$.complete(); }

  onFilterChange(f: ProductFilter) {
    this.filter = { ...f, size: this.pageSize };
    this.filter$.next(this.filter);
  }

  onPageSizeChange() {
    this.filter = { ...this.filter, size: this.pageSize, page: 0 };
    this.filter$.next(this.filter);
  }

  goToPage(p: number) {
    if (p < 0 || p >= this.totalPages) return;
    this.filter = { ...this.filter, page: p };
    this.filter$.next(this.filter);
  }

  onProductSelected(id: number) { this.router.navigate(['/products', id]); }

  onResetFilter() { this.filter = { page: 0, size: this.pageSize }; this.filter$.next(this.filter); }

  get pageRange(): number[] {
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages - 1, start + 4);
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }
}
