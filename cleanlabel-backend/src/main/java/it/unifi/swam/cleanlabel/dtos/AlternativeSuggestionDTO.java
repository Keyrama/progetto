package it.unifi.swam.cleanlabel.dtos;

import lombok.*;

/**
 * Pure output DTO — no backing entity.
 * Produced at runtime by AlternativeSuggestionService
 * by comparing products in the same category.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlternativeSuggestionDTO {
    private Long productId;
    private String productName;
    private String brand;
    private Integer healthScore;
    private boolean cleanLabel;
    /** Positive value: how many points better than the source product */
    private Integer scoreDelta;
    /** Human-readable reason why this product is a better choice */
    private String reason;
}