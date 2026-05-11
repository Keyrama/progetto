package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.ProductClaim;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductClaimDTO {

    private Long id;
    private String rawLabel;
    private ProductClaim.AnalysisStatus status;

    private ClaimDefinitionDTO claimDefinition;

    private ValidationResultDTO validationResult;

    private LocalDateTime analyzedAt;
}