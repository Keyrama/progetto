import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs/operators';
import { ProductDTO, ProductFilter, ProductPageFilter } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: ProductDTO[] = [];
  loading = false;

  // Server-side pagination state
  currentPage = 0;
  pageSize = 12;
  totalElements = 0;
  totalPages = 0;

  filter: ProductFilter = {};

  private filter$ = new Subject<ProductPageFilter>();
  private destroy$ = new Subject<void>();

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.filter$.pipe(
      debounceTime(300),
      distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
      switchMap(f => {
        this.loading = true;
        return this.productService.getProducts(f);
      }),
      takeUntil(this.destroy$)
    ).subscribe(page => {
      this.products     = page.content;
      this.totalElements = page.totalElements;
      this.totalPages   = page.totalPages;
      this.currentPage  = page.number;
      this.loading = false;
    });

    this.emitFilter();
  }

  ngOnDestroy() { this.destroy$.next(); this.destroy$.complete(); }

  onFilterChange(f: ProductFilter) {
    this.filter = { ...f };
    this.currentPage = 0;   // reset to first page on new filter
    this.emitFilter();
  }

  onResetFilter() {
    this.filter = {};
    this.currentPage = 0;
    this.emitFilter();
  }

  goToPage(p: number) {
    if (p < 0 || p >= this.totalPages) return;
    this.currentPage = p;
    this.emitFilter();
  }

  onProductSelected(id: number) { this.router.navigate(['/products', id]); }

  get pageRange(): number[] {
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages - 1, start + 4);
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }

  scoreClass(score: number | undefined): string {
    if (score == null) return 'score-unknown';
    if (score >= 70) return 'score-high';
    if (score >= 40) return 'score-medium';
    return 'score-low';
  }

  private emitFilter() {
    this.filter$.next({
      ...this.filter,
      page: this.currentPage,
      size: this.pageSize,
      sort: 'name,asc',
    });
  }
}
