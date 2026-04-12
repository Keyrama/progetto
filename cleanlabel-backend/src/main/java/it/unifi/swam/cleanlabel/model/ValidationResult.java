package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Value object embedded in ProductClaim.
 * Stores the outcome of the dynamic validation of a claim
 * against the actual product data (ingredients + nutritional values).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ValidationResult {

    public enum Verdict {
        /** Claim is supported by the product's actual data */
        CONFIRMED,
        /** Claim is contradicted by the product's actual data — most severe case */
        CONTRADICTED,
        /** No automatic validation rule applies (subjective/marketing claim) */
        UNVERIFIABLE,
        /** Insufficient data to validate (e.g. fiber value is null) */
        INCOMPLETE_DATA
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "verdict")
    private Verdict verdict;

    /** Human-readable explanation generated dynamically by the validator */
    @Column(name = "validation_detail", length = 500)
    private String validationDetail;
}