package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.ProductClaim;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductClaimDTO {

    private Long id;
    private String rawLabel;
    private ProductClaim.AnalysisStatus status;

    /**
     * Null when status = UNMATCHED.
     * Contains the full definition from the library when MATCHED,
     * including whether the claim is misleading and why.
     */
    private ClaimDefinitionDTO claimDefinition;

    /**
     * Result of the dynamic validation against the product's
     * actual ingredients and nutritional values.
     */
    private ValidationResultDTO validationResult;

    private LocalDateTime analyzedAt;
}