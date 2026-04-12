import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import {
  ProductDTO, ProductCategoryDTO, IngredientDTO,
  AllergenDTO, AlternativeSuggestionDTO,
  ProductClaimDTO, ClaimAnalysisRequestDTO, ProductFilter
} from '../models/product.model';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private api = environment.apiUrl;
  useMock = false;

  constructor(private http: HttpClient, private auth: AuthService) {}

  // ── Products ──────────────────────────────────────────────────────────────

  /**
   * Returns the full product list.
   * The backend returns a plain List<ProductDTO> — pagination is done client-side.
   */
  getProducts(filter: ProductFilter = {}): Observable<ProductDTO[]> {
    if (this.useMock) return of([]);
    let params = new HttpParams();
    if (filter.search)     params = params.set('search', filter.search);
    if (filter.category)   params = params.set('category', filter.category.toString());
    if (filter.cleanLabel) params = params.set('cleanLabel', 'true');

    return this.http.get<ProductDTO[]>(`${this.api}/products`, { params })
      .pipe(catchError(() => { this.useMock = true; return of([]); }));
  }

  getProduct(id: number): Observable<ProductDTO> {
    return this.http.get<ProductDTO>(`${this.api}/products/${id}`);
  }

  /** Requires CORPORATE role — sends X-Mock-User-Role header */
  createProduct(data: ProductDTO): Observable<ProductDTO> {
    return this.http.post<ProductDTO>(`${this.api}/products`, data, {
      headers: this.auth.roleHeader
    });
  }

  /** Requires CORPORATE role */
  updateProduct(id: number, data: ProductDTO): Observable<ProductDTO> {
    return this.http.put<ProductDTO>(`${this.api}/products/${id}`, data, {
      headers: this.auth.roleHeader
    });
  }

  /** Requires CORPORATE role */
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/products/${id}`, {
      headers: this.auth.roleHeader
    });
  }

  // ── Alternatives ──────────────────────────────────────────────────────────

  getAlternatives(id: number, limit = 5): Observable<AlternativeSuggestionDTO[]> {
    return this.http.get<AlternativeSuggestionDTO[]>(
      `${this.api}/products/${id}/alternatives`,
      { params: new HttpParams().set('limit', limit.toString()) }
    ).pipe(catchError(() => of([])));
  }

  // ── Claim analysis ────────────────────────────────────────────────────────

  /**
   * Requires SPECIALIST or CORPORATE role.
   * Sends raw claim strings, receives analysis results with
   * misleading flags and dynamic validation verdicts.
   */
  analyzeClaims(productId: number, rawClaims: string[]): Observable<ProductClaimDTO[]> {
    const body: ClaimAnalysisRequestDTO = { rawClaims };
    return this.http.post<ProductClaimDTO[]>(
      `${this.api}/products/${productId}/claims/analyze`,
      body,
      { headers: this.auth.roleHeader }
    );
  }

  getClaims(productId: number, misleadingOnly = false): Observable<ProductClaimDTO[]> {
    let params = new HttpParams();
    if (misleadingOnly) params = params.set('misleading', 'true');
    return this.http.get<ProductClaimDTO[]>(
      `${this.api}/products/${productId}/claims`, { params }
    ).pipe(catchError(() => of([])));
  }

  // ── Categories ────────────────────────────────────────────────────────────

  getCategories(): Observable<ProductCategoryDTO[]> {
    if (this.useMock) return of([]);
    return this.http.get<ProductCategoryDTO[]>(`${this.api}/categories`)
      .pipe(catchError(() => { this.useMock = true; return of([]); }));
  }

  // ── Ingredients ───────────────────────────────────────────────────────────

  getIngredients(): Observable<IngredientDTO[]> {
    return this.http.get<IngredientDTO[]>(`${this.api}/ingredients`)
      .pipe(catchError(() => of([])));
  }

  // ── Allergens ─────────────────────────────────────────────────────────────

  getAllergens(): Observable<AllergenDTO[]> {
    return this.http.get<AllergenDTO[]>(`${this.api}/allergens`)
      .pipe(catchError(() => of([])));
  }
}
