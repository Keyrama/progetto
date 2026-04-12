package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimDefinitionDTO {

    private Long id;
    private String term;
    private ClaimDefinition.ClaimType claimType;
    private boolean regulated;
    private boolean misleading;
    private ClaimDefinition.MisleadingReason misleadingReason;
    private String explanation;
    private String regulatoryReference;
    private ClaimDefinition.ValidationStrategy validationStrategy;
    private Double validationThreshold;
}