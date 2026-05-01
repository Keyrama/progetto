package it.unifi.swam.cleanlabel.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Unified DTO for Product.
 *
 * Input (POST/PUT): name, brand, description, categoryId, nutritionalValue,
 *   ingredientIds, mayContainAllergenIds, sustainabilityScore.
 *   Fields like healthScore, cleanLabel, declaredAllergens are COMPUTED by the system.
 *
 * Output (GET): all of the above plus the computed fields.
 *
 * Claims are NOT included here. They are the result of an explicit analysis
 * and are retrieved separately via GET /api/products/{id}/claims.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDTO {

    // ── Common fields (both input and output) ─────────────────────────────────

    private Long id;

    @NotBlank @Size(max = 200)
    private String name;

    @NotBlank @Size(max = 200)
    private String brand;

    @Size(max = 1000)
    private String description;

    /** ID of the ProductCategory. Required on input; included in output. */
    private Long categoryId;

    /** Category name — populated on output only */
    private String categoryName;

    @Valid
    private NutritionalValueDTO nutritionalValue;

    /**
     * Input: list of existing Ingredient IDs to associate.
     * Output: full ingredient details.
     * We use separate fields to avoid overloading one field with two types.
     */
    private List<Long> ingredientIds;
    private List<IngredientDTO> ingredients;

    /** Input: allergen IDs for "may contain traces of" */
    private List<Long> mayContainAllergenIds;

    /** Output: full allergen details for "may contain traces of" */
    private List<AllergenDTO> mayContainAllergens;

    private Integer sustainabilityScore;

    // ── Output-only (computed by the system) ─────────────────────────────────

    /** Computed by HealthScoreService from nutritional values and ingredient risk */
    private Integer healthScore;

    /** True if no artificial ingredients and no HIGH-risk additives */
    private boolean cleanLabel;

    /**
     * Allergens derived from the ingredient list (not "may contain").
     * Computed at read time from ingredient.allergens — not stored separately.
     */
    private List<AllergenDTO> declaredAllergens;
}