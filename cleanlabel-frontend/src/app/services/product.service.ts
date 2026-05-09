import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {
  ProductDTO, ProductCategoryDTO, IngredientDTO,
  AllergenDTO, AlternativeSuggestionDTO,
  ProductClaimDTO, ClaimAnalysisRequestDTO,
  ClaimDefinitionDTO, ProductFilter
} from '../models/product.model';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import { ProductCriteria } from './filters/product-criteria';
import { IngredientCriteria } from './filters/ingredient-criteria';
import { CategoryCriteria } from './filters/category-criteria';
import { ClaimDefinitionCriteria } from './filters/claim-definition-criteria';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private api = environment.apiUrl;

  constructor(private http: HttpClient, private auth: AuthService) {}

  // ── Products ──────────────────────────────────────────────────────────────

  getProducts(filter: ProductFilter = {}, criteria?: ProductCriteria): Observable<ProductDTO[]> {
    let params = criteria ? criteria.toParams() : new HttpParams();
    if (filter.search)     params = params.set('search', filter.search);
    if (filter.category)   params = params.set('category', filter.category.toString());
    if (filter.cleanLabel) params = params.set('cleanLabel', 'true');
    return this.http.get<ProductDTO[]>(`${this.api}/products`, { params })
      .pipe(catchError(() => of([])));
  }

  getProductsCount(filter: ProductFilter = {}): Observable<number> {
    let params = new HttpParams();
    if (filter.search)     params = params.set('search', filter.search);
    if (filter.category)   params = params.set('category', filter.category.toString());
    if (filter.cleanLabel) params = params.set('cleanLabel', 'true');
    return this.http.get<number>(`${this.api}/products/count`, { params })
      .pipe(catchError(() => of(0)));
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

  getCategories(criteria?: CategoryCriteria): Observable<ProductCategoryDTO[]> {
    const params = criteria ? criteria.toParams() : new HttpParams();
    return this.http.get<ProductCategoryDTO[]>(`${this.api}/categories`, { params })
      .pipe(catchError(() => of([])));
  }

  getCategoriesCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/categories/count`)
      .pipe(catchError(() => of(0)));
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

  getIngredients(criteria?: IngredientCriteria): Observable<IngredientDTO[]> {
    const params = criteria ? criteria.toParams() : new HttpParams();
    return this.http.get<IngredientDTO[]>(`${this.api}/ingredients`, { params })
      .pipe(catchError(() => of([])));
  }

  getIngredientsCount(): Observable<number> {
    return this.http.get<number>(`${this.api}/ingredients/count`)
      .pipe(catchError(() => of(0)));
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

  // ── Claim definitions library ─────────────────────────────────────────────

  getClaimDefinitions(misleading?: boolean, type?: string, criteria?: ClaimDefinitionCriteria): Observable<ClaimDefinitionDTO[]> {
    let params = criteria ? criteria.toParams() : new HttpParams();
    if (misleading != null) params = params.set('misleading', misleading.toString());
    if (type)               params = params.set('type', type);
    return this.http.get<ClaimDefinitionDTO[]>(`${this.api}/claims/definitions`, { params })
      .pipe(catchError(() => of([])));
  }

  getClaimDefinitionsCount(misleading?: boolean, type?: string, search?: string): Observable<number> {
    let params = new HttpParams();
    if (misleading != null) params = params.set('misleading', misleading.toString());
    if (type)               params = params.set('type', type);
    if (search)             params = params.set('search', search);
    return this.http.get<number>(`${this.api}/claims/definitions/count`, { params })
      .pipe(catchError(() => of(0)));
  }

  createClaimDefinition(data: ClaimDefinitionDTO): Observable<ClaimDefinitionDTO> {
    return this.http.post<ClaimDefinitionDTO>(`${this.api}/claims/definitions`, data, {
      headers: this.auth.roleHeader
    });
  }

  updateClaimDefinition(id: number, data: ClaimDefinitionDTO): Observable<ClaimDefinitionDTO> {
    return this.http.put<ClaimDefinitionDTO>(`${this.api}/claims/definitions/${id}`, data, {
      headers: this.auth.roleHeader
    });
  }

  deleteClaimDefinition(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/claims/definitions/${id}`, {
      headers: this.auth.roleHeader
    });
  }
}
