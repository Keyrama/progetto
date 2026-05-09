import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs/operators';
import { ProductDTO, ProductFilter } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { ProductCriteria } from '../../services/filters/product-criteria';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: ProductDTO[] = [];
  filter: ProductFilter = {};
  loading = false;

  criteria = new ProductCriteria(0, 6);
  totalElements = 0;
  totalPages = 0;
  currentPage = 0;

  private filter$ = new Subject<ProductFilter>();
  private destroy$ = new Subject<void>();

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.filter$.pipe(
      debounceTime(300),
      distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
      switchMap(f => {
        this.loading = true;
        this.criteria.offset = 0;
        this.currentPage = 0;
        return this.productService.getProductsCount(f);
      }),
      takeUntil(this.destroy$)
    ).subscribe(count => {
      this.totalElements = count;
      this.totalPages = Math.ceil(count / this.criteria.limit);
      this.loadPage();
    });

    this.filter$.next(this.filter);
  }

  ngOnDestroy() { this.destroy$.next(); this.destroy$.complete(); }

  onFilterChange(f: ProductFilter) {
    this.filter = { ...f };
    this.filter$.next(this.filter);
  }

  onResetFilter() {
    this.filter = {};
    this.filter$.next(this.filter);
  }

  goToPage(p: number) {
    if (p < 0 || p >= this.totalPages) return;
    this.currentPage = p;
    this.criteria.offset = p * this.criteria.limit;
    this.loadPage();
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

  private loadPage() {
    this.loading = true;
    this.productService.getProducts(this.filter, this.criteria)
      .pipe(takeUntil(this.destroy$))
      .subscribe(products => {
        this.products = products;
        this.loading = false;
      });
  }
}
