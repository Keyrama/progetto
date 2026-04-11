export interface ProductCategory {
  id: number;
  name: string;
  description?: string;
}

export interface NutritionalValue {
  calories: number;
  proteins: number;
  carbohydrates: number;
  sugars: number;
  fats: number;
  saturatedFats: number;
  salt: number;
  fiber?: number;
}

export interface Allergen {
  id: number;
  name: string;
  code: string;
}

export interface Ingredient {
  id: number;
  name: string;
  eNumber?: string;
  isArtificial: boolean;
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH';
}

export interface NutritionalClaim {
  id: number;
  label: string;
  isValidated: boolean;
  isMisleading: boolean;
  explanation?: string;
}

export interface ProductSummary {
  id: number;
  name: string;
  brand: string;
  description?: string;
  healthScore: number;
  sustainabilityScore: number;
  isCleanLabel: boolean;
  category?: ProductCategory;
}

export interface ProductDetail extends ProductSummary {
  nutritionalValue?: NutritionalValue;
  allergens: Allergen[];
  ingredients: Ingredient[];
  claims: NutritionalClaim[];
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface ProductFilter {
  search?: string;
  categoryId?: number;
  cleanLabelOnly?: boolean;
  minScore?: number;
  page?: number;
  size?: number;
}

export interface HealthScoreDTO {
  totalScore: number;
  nutritionScore: number;
  ingredientScore: number;
  claimScore: number;
  flaggedIngredients: string[];
  misleadingClaims: string[];
  summary: string;
}

export interface AlternativeSuggestion {
  id: number;
  targetProduct: ProductSummary;
  reason: string;
  scoreDelta: number;
}
