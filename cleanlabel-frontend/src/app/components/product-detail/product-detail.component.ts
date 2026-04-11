import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductDetail } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
})
export class ProductDetailComponent implements OnInit {
  product: ProductDetail | null = null;
  score: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(id).subscribe(p => {
      this.product = p;
    });
    this.productService.getHealthScore(id).subscribe(s => {
      this.score = s;
    });
  }

  get totalScoreClass(): string {
    if (!this.score) return '';
    if (this.score.totalScore >= 70) return 'score-high';
    if (this.score.totalScore >= 40) return 'score-medium';
    return 'score-low';
  }

  progressClass(value: number, max: number): string {
    const pct = value / max;
    if (pct >= 0.7) return 'bg-success';
    if (pct >= 0.4) return 'bg-warning';
    return 'bg-danger';
  }

  riskBadgeClass(level: string): string {
    if (level === 'LOW')    return 'bg-success';
    if (level === 'MEDIUM') return 'bg-warning text-dark';
    return 'bg-danger';
  }
}
