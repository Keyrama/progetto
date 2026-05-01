import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, switchMap, forkJoin} from 'rxjs';
import { DatePipe } from '@angular/common';
import { ProductDTO, ProductClaimDTO, Verdict } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  product: ProductDTO | null = null;
  loading = true;

  // Claims live as independent session state — not inside product.
  // Loaded from the backend on each product change, replaced on new analysis.
  claims: ProductClaimDTO[] = [];
  loadingClaims = false;

  claimInput = '';
  analyzingClaims = false;
  claimError = '';

  private routeSub!: Subscription;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private productService: ProductService,
    public auth: AuthService,
    private datePipe: DatePipe
  ) {}

  ngOnInit() {
    // paramMap observable reacts to ID changes without destroying the component.
    // On each navigation, product and claims are both reset immediately,
    // then reloaded in parallel via forkJoin to avoid sequential waterfalls.
    this.routeSub = this.route.paramMap.pipe(
      switchMap(params => {
        const id = Number(params.get('id'));
        this.loading = true;
        this.product = null;
        this.claims = [];
        this.claimInput = '';
        this.claimError = '';
        return forkJoin({
          product: this.productService.getProduct(id),
          claims:  this.productService.getClaims(id),
        });
      })
    ).subscribe(({ product, claims }) => {
      this.product = product;
      this.claims  = claims;
      this.loading = false;
    });
  }

  ngOnDestroy() {
    this.routeSub?.unsubscribe();
  }

  scoreClass(score: number | undefined): string {
    if (score == null) return 'text-muted';
    if (score >= 70) return 'text-success';
    if (score >= 40) return 'text-warning';
    return 'text-danger';
  }

  riskBadgeClass(level: string): string {
    if (level === 'LOW')    return 'bg-success';
    if (level === 'MEDIUM') return 'bg-warning text-dark';
    return 'bg-danger';
  }

  get canAnalyze(): boolean {
    return this.auth.hasRole('SPECIALIST', 'CORPORATE');
  }

  analyzeClaims() {
    if (!this.product?.id || !this.claimInput.trim()) return;

    const rawClaims = this.claimInput.split(',').map(s => s.trim()).filter(Boolean);
    this.analyzingClaims = true;
    this.claimError = '';

    this.productService.analyzeClaims(this.product.id, rawClaims).subscribe({
      next: claims => {
        this.claims = claims; // replace session state — never touches product
        this.analyzingClaims = false;
        this.claimInput = '';
      },
      error: () => {
        this.claimError = 'Analisi non riuscita. Verifica il ruolo e la connessione al backend.';
        this.analyzingClaims = false;
      }
    });
  }

  verdictClass(verdict: Verdict): string {
    const map: Record<Verdict, string> = {
      CONFIRMED:       'text-success',
      CONTRADICTED:    'text-danger',
      UNVERIFIABLE:    'text-secondary',
      INCOMPLETE_DATA: 'text-warning',
    };
    return map[verdict] ?? 'text-secondary';
  }

  verdictIcon(verdict: Verdict): string {
    const map: Record<Verdict, string> = {
      CONFIRMED:       'bi-check-circle-fill',
      CONTRADICTED:    'bi-x-circle-fill',
      UNVERIFIABLE:    'bi-question-circle',
      INCOMPLETE_DATA: 'bi-exclamation-triangle',
    };
    return map[verdict] ?? 'bi-question-circle';
  }

  verdictLabel(verdict: Verdict): string {
    const map: Record<Verdict, string> = {
      CONFIRMED:       'Confermato',
      CONTRADICTED:    'Contraddetto',
      UNVERIFIABLE:    'Non verificabile',
      INCOMPLETE_DATA: 'Dati incompleti',
    };
    return map[verdict] ?? verdict;
  }

  /**
   * Returns the formatted analyzedAt of the first claim.
   * All claims in an analysis share the same timestamp (set in ClaimAnalysisService),
   * so showing it once in the section header is correct.
   */
  get lastAnalyzedAt(): string | null {
    if (!this.claims.length) return null;
    return this.datePipe.transform(this.claims[0].analyzedAt, 'dd/MM/yyyy HH:mm');
  }
}
