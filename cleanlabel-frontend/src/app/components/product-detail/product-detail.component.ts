import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductDTO, ProductClaimDTO, Verdict } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
})
export class ProductDetailComponent implements OnInit {
  product: ProductDTO | null = null;
  loading = true;

  // Claim analysis
  claimInput = '';
  analyzingClaims = false;
  claimError = '';

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private productService: ProductService,
    public auth: AuthService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(id).subscribe(p => {
      this.product = p;
      this.loading = false;
    });
  }

  // ── Score helpers ────────────────────────────────────────────────────────

  scoreClass(score: number | undefined): string {
    if (score == null) return '';
    if (score >= 70) return 'score-high';
    if (score >= 40) return 'score-medium';
    return 'score-low';
  }

  scoreLabel(score: number | undefined): string {
    if (score == null) return 'N/D';
    if (score >= 70) return 'Consigliato';
    if (score >= 40) return 'Moderato';
    return 'Sconsigliato';
  }

  riskBadgeClass(level: string): string {
    if (level === 'LOW')    return 'bg-success';
    if (level === 'MEDIUM') return 'bg-warning text-dark';
    return 'bg-danger';
  }

  // ── Claim analysis ────────────────────────────────────────────────────────

  /** True if the current user can trigger claim analysis */
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
        if (this.product) this.product.claims = claims;
        this.analyzingClaims = false;
        this.claimInput = '';
      },
      error: () => {
        this.claimError = 'Analisi non riuscita. Verifica il ruolo e la connessione al backend.';
        this.analyzingClaims = false;
      }
    });
  }

  // ── Claim display helpers ─────────────────────────────────────────────────

  verdictClass(verdict: Verdict): string {
    const map: Record<Verdict, string> = {
      CONFIRMED:      'text-success',
      CONTRADICTED:   'text-danger',
      UNVERIFIABLE:   'text-secondary',
      INCOMPLETE_DATA:'text-warning',
    };
    return map[verdict] ?? 'text-secondary';
  }

  verdictIcon(verdict: Verdict): string {
    const map: Record<Verdict, string> = {
      CONFIRMED:      'bi-check-circle-fill',
      CONTRADICTED:   'bi-x-circle-fill',
      UNVERIFIABLE:   'bi-question-circle',
      INCOMPLETE_DATA:'bi-exclamation-triangle',
    };
    return map[verdict] ?? 'bi-question-circle';
  }

  verdictLabel(verdict: Verdict): string {
    const map: Record<Verdict, string> = {
      CONFIRMED:      'Confermato',
      CONTRADICTED:   'Contraddetto',
      UNVERIFIABLE:   'Non verificabile',
      INCOMPLETE_DATA:'Dati incompleti',
    };
    return map[verdict] ?? verdict;
  }
}
