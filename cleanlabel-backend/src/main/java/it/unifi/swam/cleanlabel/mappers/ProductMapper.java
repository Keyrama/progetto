package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.*;
import it.unifi.swam.cleanlabel.model.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps between Product domain entities and DTOs.
 * Provides both a summary mapping (for list views) and a full mapping (for detail views).
 */
public class ProductMapper {




    /**
     * Maps a Product to a summary DTO (no ingredients, allergens, or claims).
     * Used for list/catalogue views.
     */
    public static ProductDTO toSummaryDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setHealthScore(product.getHealthScore());
        dto.setSustainabilityScore(product.getSustainabilityScore());
        dto.setCleanLabel(product.isCleanLabel());
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }
        return dto;
    }

    /**
     * Maps a Product to a full detail DTO including all nested entities.
     * Used for single-product detail views.
     */
    public static ProductDTO toDetailDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = toSummaryDTO(product);

        // Nutritional values
        if (product.getNutritionalValue() != null) {
            dto.setNutritionalValue(NutritionalValueMapper.toDTO(product.getNutritionalValue()));
        }

        // Ingredients
        if (product.getIngredients() != null) {
            dto.setIngredients(product.getIngredients().stream()
                .map(IngredientMapper::toDTO)
                .collect(Collectors.toList()));
        } else {
            dto.setIngredients(Collections.emptyList());
        }

        // Allergens
        if (product.getAllergens() != null) {
            dto.setAllergens(product.getAllergens().stream()
                .map(AllergenMapper::toDTO)
                .collect(Collectors.toList()));
        } else {
            dto.setAllergens(Collections.emptyList());
        }

        // Claims
        if (product.getClaims() != null) {
            dto.setClaims(product.getClaims().stream()
                .map(NutritionalClaimMapper::toDTO)
                .collect(Collectors.toList()));
        } else {
            dto.setClaims(Collections.emptyList());
        }

        return dto;
    }

    /**
     * Maps a ProductDTO back to a Product entity.
     * Used for POST/PUT operations.
     */
    public static Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setHealthScore(dto.getHealthScore());
        product.setSustainabilityScore(dto.getSustainabilityScore());
        product.setCleanLabel(dto.isCleanLabel());
        return product;
    }

    public static List<ProductDTO> toSummaryDTOList(List<Product> products) {
        if (products == null) return Collections.emptyList();
        return products.stream().map(ProductMapper::toSummaryDTO).collect(Collectors.toList());
    }
}
