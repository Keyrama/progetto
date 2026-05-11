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

    private Long categoryId;

    private String categoryName;

    @Valid
    private NutritionalValueDTO nutritionalValue;

    private List<Long> ingredientIds;
    private List<IngredientDTO> ingredients;

    private List<Long> mayContainAllergenIds;

    private List<AllergenDTO> mayContainAllergens;

    private Integer sustainabilityScore;

    private Integer healthScore;

    private boolean cleanLabel;

    private List<AllergenDTO> declaredAllergens;
}