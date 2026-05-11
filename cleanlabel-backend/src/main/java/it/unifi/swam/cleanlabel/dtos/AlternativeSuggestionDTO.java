package it.unifi.swam.cleanlabel.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlternativeSuggestionDTO {
    private Long productId;
    private String productName;
    private String brand;
    private Integer healthScore;
    private boolean cleanLabel;
    private Integer scoreDelta;
    private String reason;
}