package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.Ingredient;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IngredientDTO {

    private Long id;

    private String name;

    private String additiveCode;

    private String description;

    private boolean artificial;

    private Ingredient.RiskLevel riskLevel;

    /** Allergens contained in this ingredient (derived, not input by user) */
    private List<AllergenDTO> allergens;
}