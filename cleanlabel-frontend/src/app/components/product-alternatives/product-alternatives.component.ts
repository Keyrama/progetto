import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlternativeSuggestionDTO } from '../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-alternatives',
  templateUrl: './product-alternatives.component.html',
})
export class ProductAlternativesComponent implements OnInit {
  @Input() productId!: number;

  alternatives: AlternativeSuggestionDTO[] = [];
  loading = true;

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.productService.getAlternatives(this.productId).subscribe(alts => {
      this.alternatives = alts;
      this.loading = false;
    });
  }

  scoreClass(score: number): string {
    if (score >= 70) return 'score-high';
    if (score >= 40) return 'score-medium';
    return 'score-low';
  }

  goToProduct(id: number) {
    this.router.navigate(['/products', id]);
  }
}
