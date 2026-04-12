package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.ValidationResult;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ValidationResultDTO {
    private ValidationResult.Verdict verdict;
    private String validationDetail;
}