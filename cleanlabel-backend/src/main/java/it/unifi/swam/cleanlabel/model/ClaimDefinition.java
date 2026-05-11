package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "claim_definitions")
public class ClaimDefinition {

    public enum ClaimType {
        NUTRITIONAL,
        HEALTH,
        MARKETING
    }

    public enum MisleadingReason {
        NO_LEGAL_DEFINITION,
        VAGUE_CRITERIA,
        REGULATORY_BREACH,
        NONE
    }

    public enum ValidationStrategy {
        NONE,
        NO_ARTIFICIAL_INGREDIENTS,
        SUGAR_BELOW_THRESHOLD,
        FAT_REDUCED,
        HIGH_FIBER,
        NO_HIGH_RISK_INGREDIENTS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String term;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    private ClaimType claimType;

    @Column(name = "is_regulated", nullable = false)
    private boolean regulated = false;

    @Column(name = "is_misleading", nullable = false)
    private boolean misleading = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "misleading_reason", nullable = false)
    private MisleadingReason misleadingReason = MisleadingReason.NONE;

    @Column(nullable = false, length = 1000)
    private String explanation;

    @Column(name = "regulatory_reference", length = 300)
    private String regulatoryReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_strategy", nullable = false)
    private ValidationStrategy validationStrategy = ValidationStrategy.NONE;

    @Column(name = "validation_threshold")
    private Double validationThreshold;
}