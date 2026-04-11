package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.model.Ingredient;

public class IngredientMapper {

    public static IngredientDTO toDTO(Ingredient ingredient) {
        if (ingredient == null) return null;

        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setENumber(ingredient.getENumber());
        dto.setDescription(ingredient.getDescription());
        dto.setArtificial(ingredient.isArtificial());
        dto.setRiskLevel(ingredient.getRiskLevel() != null
            ? ingredient.getRiskLevel().name() : null);
        return dto;
    }

    public static Ingredient toEntity(IngredientDTO dto) {
        if (dto == null) return null;

        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setENumber(dto.getENumber());
        ingredient.setDescription(dto.getDescription());
        ingredient.setArtificial(dto.isArtificial());
        if (dto.getRiskLevel() != null) {
            ingredient.setRiskLevel(Ingredient.RiskLevel.valueOf(dto.getRiskLevel()));
        }
        return ingredient;
    }
}
