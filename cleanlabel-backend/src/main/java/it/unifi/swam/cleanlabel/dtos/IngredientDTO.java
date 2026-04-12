package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IngredientDTO {

    private Long id;

    @NotBlank @Size(max = 200)
    private String name;

    @Size(max = 10)
    private String eNumber;

    @Size(max = 1000)
    private String description;

    private boolean artificial;

    private Ingredient.RiskLevel riskLevel;

    /** Allergens contained in this ingredient (derived, not input by user) */
    private List<AllergenDTO> allergens;
}