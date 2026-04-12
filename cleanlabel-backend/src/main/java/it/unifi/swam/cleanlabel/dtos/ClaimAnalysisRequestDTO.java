package it.unifi.swam.cleanlabel.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * Payload for POST /api/products/{id}/claims/analyze
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimAnalysisRequestDTO {
    @NotEmpty
    private List<String> rawClaims;
}