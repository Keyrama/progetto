import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
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

  createProduct(data: ProductDTO): Observable<ProductDTO> {
    return this.http.post<ProductDTO>(`${this.api}/products`, data, {
      headers: this.auth.roleHeader
    });
  }

  updateProduct(id: number, data: ProductDTO): Observable<ProductDTO> {
    return this.http.put<ProductDTO>(`${this.api}/products/${id}`, data, {
      headers: this.auth.roleHeader
    });
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/products/${id}`, {
      headers: this.auth.roleHeader
    });
  }

  // ── Categories ────────────────────────────────────────────────────────────

  getCategories(): Observable<ProductCategoryDTO[]> {
    if (this.useMock) return of([]);
    return this.http.get<ProductCategoryDTO[]>(`${this.api}/categories`)
      .pipe(catchError(() => { this.useMock = true; return of([]); }));
  }

  createCategory(data: ProductCategoryDTO): Observable<ProductCategoryDTO> {
    return this.http.post<ProductCategoryDTO>(`${this.api}/categories`, data, {
      headers: this.auth.roleHeader
    });
  }

  updateCategory(data: ProductCategoryDTO, id?: number): Observable<ProductCategoryDTO> {
    return this.http.put<ProductCategoryDTO>(`${this.api}/categories/${id}`, data, {
      headers: this.auth.roleHeader
    });
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/categories/${id}`, {
      headers: this.auth.roleHeader
    });
  }

  // ── Ingredients ───────────────────────────────────────────────────────────

  getIngredients(): Observable<IngredientDTO[]> {
    return this.http.get<IngredientDTO[]>(`${this.api}/ingredients`)
      .pipe(catchError(() => of([])));
  }

  createIngredient(data: IngredientDTO): Observable<IngredientDTO> {
    return this.http.post<IngredientDTO>(`${this.api}/ingredients`, data, {
      headers: this.auth.roleHeader
    });
  }

  updateIngredient(id: number, data: IngredientDTO): Observable<IngredientDTO> {
    return this.http.put<IngredientDTO>(`${this.api}/ingredients/${id}`, data, {
      headers: this.auth.roleHeader
    });
  }

  deleteIngredient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/ingredients/${id}`, {
      headers: this.auth.roleHeader
    });
  }

  // ── Allergens ─────────────────────────────────────────────────────────────

  getAllergens(): Observable<AllergenDTO[]> {
    return this.http.get<AllergenDTO[]>(`${this.api}/allergens`)
      .pipe(catchError(() => of([])));
  }

  // ── Alternatives ──────────────────────────────────────────────────────────

  getAlternatives(id: number, limit = 5): Observable<AlternativeSuggestionDTO[]> {
    return this.http.get<AlternativeSuggestionDTO[]>(
      `${this.api}/products/${id}/alternatives`,
      { params: new HttpParams().set('limit', limit.toString()) }
    ).pipe(catchError(() => of([])));
  }

  // ── Claim analysis ────────────────────────────────────────────────────────

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
}
