import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {
  ProductSummary, ProductDetail, ProductCategory,
  PagedResponse, ProductFilter
} from '../models/product.model';
import { environment } from '../../environments/environment';

const MOCK_CATEGORIES: ProductCategory[] = [
  { id: 1, name: 'Cereali',   description: 'Cereali da colazione e muesli' },
  { id: 2, name: 'Snacks',    description: 'Patatine, cracker e snack salati' },
  { id: 3, name: 'Bevande',   description: 'Succhi, bibite e bevande energetiche' },
  { id: 4, name: 'Latticini', description: 'Yogurt, formaggi e derivati' },
];

const MOCK_PRODUCTS: ProductSummary[] = [
  { id: 1, name: 'Muesli Bio Integrale',     brand: 'NaturaBio',  healthScore: 88, sustainabilityScore: 88, isCleanLabel: true,  category: MOCK_CATEGORIES[0], description: 'Muesli a base di avena integrale, miele e frutta secca. 100% naturale.' },
  { id: 2, name: 'Yogurt Greco Naturale',    brand: 'LattePuro',  healthScore: 82, sustainabilityScore: 72, isCleanLabel: true,  category: MOCK_CATEGORIES[3], description: 'Yogurt greco ad alto contenuto proteico, senza additivi né zuccheri aggiunti.' },
  { id: 3, name: 'Succo Multifrutta',        brand: 'FruttaViva', healthScore: 48, sustainabilityScore: 55, isCleanLabel: false, category: MOCK_CATEGORIES[2], description: 'Bevanda a base di succhi concentrati con vitamina C aggiunta.' },
  { id: 4, name: 'Patatine Gusto Formaggio', brand: 'SnackTime',  healthScore: 28, sustainabilityScore: 30, isCleanLabel: false, category: MOCK_CATEGORIES[1], description: 'Patatine fritte aromatizzate con esaltatori di sapidità.' },
  { id: 5, name: 'PowerBlast Energy Drink',  brand: 'EnergyX',    healthScore: 14, sustainabilityScore: 15, isCleanLabel: false, category: MOCK_CATEGORIES[2], description: 'Bevanda energetica con caffeina, taurina e dolcificanti artificiali.' },
];

const MOCK_DETAILS: { [id: number]: ProductDetail } = {
  1: {
    ...MOCK_PRODUCTS[0],
    nutritionalValue: { calories: 370, proteins: 10, carbohydrates: 58, sugars: 18, fats: 7, saturatedFats: 1.2, salt: 0.1, fiber: 8 },
    allergens: [{ id: 1, name: 'Glutine', code: 'GLUTEN' }, { id: 3, name: 'Frutta a guscio', code: 'NUTS' }],
    ingredients: [
      { id: 1, name: 'Avena integrale', isArtificial: false, riskLevel: 'LOW' },
      { id: 2, name: 'Miele',           isArtificial: false, riskLevel: 'LOW' },
      { id: 3, name: 'Zucchero',        isArtificial: false, riskLevel: 'LOW' },
    ],
    claims: [
      { id: 1, label: '100% Naturale', isValidated: true,  isMisleading: false, explanation: 'Tutti gli ingredienti sono di origine naturale senza additivi sintetici.' },
    ],
  },
  2: {
    ...MOCK_PRODUCTS[1],
    nutritionalValue: { calories: 97, proteins: 9, carbohydrates: 4, sugars: 4, fats: 5, saturatedFats: 3.4, salt: 0.1, fiber: 0 },
    allergens: [{ id: 2, name: 'Latte', code: 'MILK' }],
    ingredients: [
      { id: 6, name: 'Latte scremato', isArtificial: false, riskLevel: 'LOW' },
    ],
    claims: [
      { id: 5, label: 'Alto contenuto proteico', isValidated: true, isMisleading: false, explanation: 'Contiene 9g di proteine per 100g.' },
    ],
  },
  3: {
    ...MOCK_PRODUCTS[2],
    nutritionalValue: { calories: 45, proteins: 0.5, carbohydrates: 10.5, sugars: 10, fats: 0.1, saturatedFats: 0, salt: 0.02, fiber: 0.5 },
    allergens: [],
    ingredients: [
      { id: 5, name: 'Acqua',        isArtificial: false, riskLevel: 'LOW' },
      { id: 3, name: 'Zucchero',     isArtificial: false, riskLevel: 'LOW' },
      { id: 9, name: 'Acido citrico', eNumber: 'E330', isArtificial: true, riskLevel: 'LOW' },
      { id: 10, name: 'Benzoato di sodio', eNumber: 'E211', isArtificial: true, riskLevel: 'HIGH' },
    ],
    claims: [
      { id: 6, label: '100% Frutta', isValidated: false, isMisleading: true, explanation: 'Contiene succo concentrato ricostruito, non succo fresco al 100%.' },
      { id: 7, label: 'Ricco di Vitamina C', isValidated: true, isMisleading: false, explanation: 'Vitamina C aggiunta (E300), confermato.' },
    ],
  },
  4: {
    ...MOCK_PRODUCTS[3],
    nutritionalValue: { calories: 530, proteins: 6, carbohydrates: 55, sugars: 1.5, fats: 32, saturatedFats: 4.5, salt: 1.8, fiber: 3 },
    allergens: [{ id: 1, name: 'Glutine', code: 'GLUTEN' }, { id: 2, name: 'Latte', code: 'MILK' }],
    ingredients: [
      { id: 7, name: 'Farina di frumento', isArtificial: false, riskLevel: 'LOW' },
      { id: 4, name: 'Sale',               isArtificial: false, riskLevel: 'LOW' },
      { id: 8, name: 'Olio di girasole',   isArtificial: false, riskLevel: 'LOW' },
      { id: 11, name: 'Glutammato monosodico', eNumber: 'E621', isArtificial: true, riskLevel: 'MEDIUM' },
      { id: 12, name: 'Giallo tramonto FCF',   eNumber: 'E110', isArtificial: true, riskLevel: 'HIGH' },
    ],
    claims: [
      { id: 8, label: 'Gusto Naturale al Formaggio', isValidated: false, isMisleading: true, explanation: 'Il gusto è ottenuto tramite aromi artificiali e E621.' },
      { id: 9, label: 'Senza Conservanti', isValidated: true, isMisleading: false, explanation: 'Confermato: non contiene conservanti chimici.' },
    ],
  },
  5: {
    ...MOCK_PRODUCTS[4],
    nutritionalValue: { calories: 11, proteins: 0, carbohydrates: 2.7, sugars: 0, fats: 0, saturatedFats: 0, salt: 0.12, fiber: 0 },
    allergens: [],
    ingredients: [
      { id: 5,  name: 'Acqua',           isArtificial: false, riskLevel: 'LOW' },
      { id: 13, name: 'Aspartame',       eNumber: 'E951', isArtificial: true, riskLevel: 'MEDIUM' },
      { id: 10, name: 'Benzoato di sodio', eNumber: 'E211', isArtificial: true, riskLevel: 'HIGH' },
      { id: 12, name: 'Giallo tramonto FCF', eNumber: 'E110', isArtificial: true, riskLevel: 'HIGH' },
    ],
    claims: [
      { id: 10, label: 'Zero Zuccheri', isValidated: true, isMisleading: false, explanation: 'Non contiene zucchero aggiunto.' },
      { id: 11, label: 'Boost Naturale di Energia', isValidated: false, isMisleading: true, explanation: "L'energia è fornita da caffeina sintetica e taurina." },
    ],
  },
};

@Injectable({ providedIn: 'root' })
export class ProductService {
  private api = environment.apiUrl;
  useMock = false;

  constructor(private http: HttpClient) {}

  getProducts(filter: ProductFilter = {}): Observable<PagedResponse<ProductSummary>> {
    if (this.useMock) return of(this.filterMock(filter));
    let params = new HttpParams()
      .set('page', (filter.page ?? 0).toString())
      .set('size', (filter.size ?? 12).toString());
    if (filter.search)         params = params.set('search', filter.search);
    if (filter.categoryId)     params = params.set('categoryId', filter.categoryId.toString());
    if (filter.cleanLabelOnly) params = params.set('cleanLabelOnly', 'true');
    if (filter.minScore != null) params = params.set('minScore', filter.minScore.toString());
    return this.http.get<PagedResponse<ProductSummary>>(`${this.api}/products`, { params })
      .pipe(catchError(() => { 
        this.useMock = true;
        console.log('Backend non raggiungibile, uso mock data');
        return of(this.filterMock(filter)); }));
  }

  getCategories(): Observable<ProductCategory[]> {
    if (this.useMock) return of(MOCK_CATEGORIES);
    return this.http.get<ProductCategory[]>(`${this.api}/categories`)
      .pipe(catchError(() => { this.useMock = true; return of(MOCK_CATEGORIES); }));
  }

  getProduct(id: number): Observable<ProductDetail> {
    if (this.useMock) return of(MOCK_DETAILS[id] ?? MOCK_DETAILS[1]);
    return this.http.get<ProductDetail>(`${this.api}/products/${id}`)
      .pipe(catchError(() => of(MOCK_DETAILS[id] ?? MOCK_DETAILS[1])));
  }

  private filterMock(filter: ProductFilter): PagedResponse<ProductSummary> {
    let results = [...MOCK_PRODUCTS];
    if (filter.search)
      results = results.filter(p =>
        p.name.toLowerCase().includes(filter.search!.toLowerCase()) ||
        p.brand.toLowerCase().includes(filter.search!.toLowerCase()));
    if (filter.categoryId)
      results = results.filter(p => p.category?.id === filter.categoryId);
    if (filter.cleanLabelOnly)
      results = results.filter(p => p.isCleanLabel);
    if (filter.minScore != null)
      results = results.filter(p => p.healthScore >= filter.minScore!);
    const page = filter.page ?? 0;
    const size = filter.size ?? 12;
    const slice = results.slice(page * size, (page + 1) * size);
    return { content: slice, totalElements: results.length, totalPages: Math.ceil(results.length / size), number: page, size };
  }

  getHealthScore(id: number): Observable<any> {
    if (this.useMock) return of(this.computeMockScore(id));
    return this.http.get<any>(`${this.api}/products/${id}/score`)
      .pipe(catchError(() => of(this.computeMockScore(id))));
  }

  private computeMockScore(id: number): any {
    const d = MOCK_DETAILS[id] ?? MOCK_DETAILS[1];
    const flagged = d.ingredients.filter((i: any) => i.riskLevel !== 'LOW').map((i: any) => i.name);
    const misleading = d.claims.filter((c: any) => c.isMisleading).map((c: any) => c.label);
    const nutritionScore = Math.max(0, 40 - (d.nutritionalValue?.sugars ?? 0) * 0.3 - (d.nutritionalValue?.salt ?? 0) * 2 - (d.nutritionalValue?.saturatedFats ?? 0) * 0.5);
    const ingredientScore = Math.max(0, 40 - flagged.length * 6);
    const claimScore = Math.max(0, 20 - misleading.length * 7);
    return {
      totalScore: d.healthScore,
      nutritionScore: Math.round(nutritionScore),
      ingredientScore: Math.round(ingredientScore),
      claimScore: Math.round(claimScore),
      flaggedIngredients: flagged,
      misleadingClaims: misleading,
      summary: d.healthScore >= 70 ? 'Prodotto consigliato' : d.healthScore >= 40 ? 'Consumo moderato' : 'Consumo sconsigliato',
    };
  }
  getAlternatives(id: number): Observable<any[]> {
    if (this.useMock) return of(this.getMockAlternatives(id));
    return this.http.get<any[]>(`${this.api}/products/${id}/alternatives`)
      .pipe(catchError(() => of(this.getMockAlternatives(id))));
  }

  private getMockAlternatives(id: number): any[] {
    const alts: { [key: number]: any[] } = {
      3: [{ id: 1, targetProduct: MOCK_PRODUCTS[0], reason: 'Nessun conservante artificiale, meno zuccheri aggiunti e più fibre.', scoreDelta: 40 }],
      4: [{ id: 2, targetProduct: MOCK_PRODUCTS[0], reason: 'Meno grassi saturi, meno sale e nessun additivo artificiale.', scoreDelta: 60 },
          { id: 3, targetProduct: MOCK_PRODUCTS[1], reason: 'Più proteine, meno grassi, nessun colorante artificiale.', scoreDelta: 54 }],
      5: [{ id: 4, targetProduct: MOCK_PRODUCTS[2], reason: 'Nessun dolcificante artificiale né coloranti ad alto rischio.', scoreDelta: 34 },
          { id: 5, targetProduct: MOCK_PRODUCTS[1], reason: 'Ingredienti naturali, alto contenuto proteico, zero additivi.', scoreDelta: 68 }],
    };
    return alts[id] ?? [];
  }

  createProduct(data: any): Observable<any> {
    if (this.useMock) return of({ ...data, id: Date.now(), healthScore: 50, sustainabilityScore: 50, isCleanLabel: false });
    return this.http.post<any>(`${this.api}/products`, data)
      .pipe(catchError(() => of({ ...data, id: Date.now(), healthScore: 50, sustainabilityScore: 50, isCleanLabel: false })));
  }

  updateProduct(id: number, data: any): Observable<any> {
    if (this.useMock) return of({ ...data, id });
    return this.http.put<any>(`${this.api}/products/${id}`, data)
      .pipe(catchError(() => of({ ...data, id })));
  }

  deleteProduct(id: number): Observable<void> {
    if (this.useMock) return of(undefined);
    return this.http.delete<void>(`${this.api}/products/${id}`)
      .pipe(catchError(() => of(undefined)));
  }

}