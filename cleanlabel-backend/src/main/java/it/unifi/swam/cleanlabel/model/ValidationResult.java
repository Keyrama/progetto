package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ValidationResult {

    public enum Verdict {
        CONFIRMED,
        CONTRADICTED,
        UNVERIFIABLE,
        INCOMPLETE_DATA
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "verdict")
    private Verdict verdict;

    @Column(name = "validation_detail", length = 500)
    private String validationDetail;
}