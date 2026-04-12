// ── Enums ─────────────────────────────────────────────────────────────────────

export type UserRole = 'CONSUMER' | 'SPECIALIST' | 'CORPORATE';
export type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH';
export type ClaimType = 'NUTRITIONAL' | 'HEALTH' | 'MARKETING';
export type ValidationStrategy =
  | 'NONE' | 'NO_ARTIFICIAL_INGREDIENTS' | 'SUGAR_BELOW_THRESHOLD'
  | 'FAT_REDUCED' | 'HIGH_FIBER' | 'NO_HIGH_RISK_INGREDIENTS';
export type MisleadingReason =
  | 'NO_LEGAL_DEFINITION' | 'VAGUE_CRITERIA' | 'REGULATORY_BREACH' | 'NONE';
export type AnalysisStatus = 'MATCHED' | 'UNMATCHED';
export type Verdict = 'CONFIRMED' | 'CONTRADICTED' | 'UNVERIFIABLE' | 'INCOMPLETE_DATA';

// ── Core entities ─────────────────────────────────────────────────────────────

export interface UserDTO {
  id?: number;
  username: string;
  email: string;
  role: UserRole;
}

export interface AllergenDTO {
  id: number;
  name: string;
  code: string;
  description?: string;
}

export interface ProductCategoryDTO {
  id: number;
  name: string;
  description?: string;
}

export interface NutritionalValueDTO {
  id?: number;
  calories: number;
  proteins: number;
  carbohydrates: number;
  sugars: number;
  fats: number;
  saturatedFats: number;
  salt: number;
  fiber?: number;
}

export interface IngredientDTO {
  id?: number;
  name: string;
  eNumber?: string;
  description?: string;
  artificial: boolean;
  riskLevel: RiskLevel;
  allergens?: AllergenDTO[];
}

// ── Claim library ─────────────────────────────────────────────────────────────

export interface ClaimDefinitionDTO {
  id?: number;
  term: string;
  claimType: ClaimType;
  regulated: boolean;
  misleading: boolean;
  misleadingReason: MisleadingReason;
  explanation: string;
  regulatoryReference?: string;
  validationStrategy: ValidationStrategy;
  validationThreshold?: number;
}

export interface ValidationResultDTO {
  verdict: Verdict;
  validationDetail: string;
}

export interface ProductClaimDTO {
  id: number;
  rawLabel: string;
  status: AnalysisStatus;
  claimDefinition?: ClaimDefinitionDTO;
  validationResult: ValidationResultDTO;
  analyzedAt: string;
}

// ── Product ───────────────────────────────────────────────────────────────────

export interface ProductDTO {
  id?: number;
  name: string;
  brand: string;
  description?: string;
  categoryId?: number;
  categoryName?: string;
  nutritionalValue?: NutritionalValueDTO;
  ingredientIds?: number[];
  ingredients?: IngredientDTO[];
  mayContainAllergenIds?: number[];
  mayContainAllergens?: AllergenDTO[];
  sustainabilityScore?: number;
  // output-only
  healthScore?: number;
  cleanLabel?: boolean;
  declaredAllergens?: AllergenDTO[];
  claims?: ProductClaimDTO[];
}

// ── Alternatives ──────────────────────────────────────────────────────────────

export interface AlternativeSuggestionDTO {
  productId: number;
  productName: string;
  brand: string;
  healthScore: number;
  cleanLabel: boolean;
  scoreDelta: number;
  reason: string;
}

// ── Filter (client-side) ──────────────────────────────────────────────────────

export interface ProductFilter {
  search?: string;
  category?: number;
  cleanLabel?: boolean;
}

// ── Claim analysis request ────────────────────────────────────────────────────

export interface ClaimAnalysisRequestDTO {
  rawClaims: string[];
}
