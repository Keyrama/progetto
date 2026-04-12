package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Master library of known nutritional and marketing claims.
 * Populated via data.sql at boot — not freely editable by end users.
 *
 * Each entry defines:
 *  - whether the claim is regulated by EU law (Reg. 1924/2006)
 *  - whether it is potentially misleading for consumers
 *  - the validation strategy used to verify it against product data
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "claim_definitions")
public class ClaimDefinition {

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum ClaimType {
        /** Regulated claims with precise EU criteria (e.g. "high fibre", "low fat") */
        NUTRITIONAL,
        /** Health relationship claims regulated by EU (e.g. "supports heart function") */
        HEALTH,
        /** Unregulated marketing terms (e.g. "natural", "light", "healthy") */
        MARKETING
    }

    public enum MisleadingReason {
        NO_LEGAL_DEFINITION,
        VAGUE_CRITERIA,
        REGULATORY_BREACH,
        NONE
    }

    /**
     * Identifies which validation logic to apply when checking this claim
     * against the actual product data (ingredients + nutritional values).
     */
    public enum ValidationStrategy {
        NONE,
        NO_ARTIFICIAL_INGREDIENTS,
        SUGAR_BELOW_THRESHOLD,
        FAT_REDUCED,
        HIGH_FIBER,
        NO_HIGH_RISK_INGREDIENTS
    }

    // ── Fields ────────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Normalized lowercase term used for matching against raw product label text.
     * E.g. "natural", "light", "high fibre", "senza zuccheri aggiunti"
     */
    @Column(nullable = false, unique = true, length = 200)
    private String term;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    private ClaimType claimType;

    /** True if the claim is regulated by EU Reg. 1924/2006 with precise criteria */
    @Column(name = "is_regulated", nullable = false)
    private boolean regulated = false;

    /** True if the claim is potentially misleading for consumers */
    @Column(name = "is_misleading", nullable = false)
    private boolean misleading = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "misleading_reason", nullable = false)
    private MisleadingReason misleadingReason = MisleadingReason.NONE;

    /** Evidence-based explanation shown to the consumer */
    @Column(nullable = false, length = 1000)
    private String explanation;

    /** Regulatory reference if applicable. E.g. "EU Reg. 1924/2006, Art. 8" */
    @Column(name = "regulatory_reference", length = 300)
    private String regulatoryReference;

    /** Which validation strategy to apply against product data */
    @Enumerated(EnumType.STRING)
    @Column(name = "validation_strategy", nullable = false)
    private ValidationStrategy validationStrategy = ValidationStrategy.NONE;

    /**
     * Numeric threshold used by certain strategies.
     * E.g. for SUGAR_BELOW_THRESHOLD: 0.5 (g/100g per EU regulation).
     * E.g. for HIGH_FIBER: 6.0 (g/100g per EU regulation).
     */
    @Column(name = "validation_threshold")
    private Double validationThreshold;
}