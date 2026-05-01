package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a specific claim found on a product label.
 * Created automatically by ClaimAnalysisService — never inserted manually.
 *
 * Stores:
 *  - the raw text as it appears on the label
 *  - the matched ClaimDefinition from the master library (if found)
 *  - the dynamic ValidationResult against the product's actual data
 *
 * Uniqueness note:
 *   The original unique constraint on (product_id, claim_definition_id) was
 *   problematic for UNMATCHED claims because claim_definition_id is NULL for
 *   those rows. SQL NULL semantics mean that two NULL values do NOT violate a
 *   unique constraint on most databases, so multiple UNMATCHED claims per
 *   product would be silently allowed.
 *
 *   The constraint is dropped in favour of application-level deduplication:
 *   ClaimAnalysisService deduplicates rawClaims before inserting, and always
 *   deletes previous results before a new analysis (full replacement).
 *   This is both correct and database-agnostic.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_claims")
public class ProductClaim {

    public enum AnalysisStatus {
        /** Term found in the ClaimDefinition library and validated */
        MATCHED,
        /** Term not found in library — requires manual specialist review */
        UNMATCHED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Null when status = UNMATCHED (term not found in library).
     * In that case rawLabel holds the original text for manual review.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_definition_id")
    private ClaimDefinition claimDefinition;

    /** Raw text of the claim exactly as it appears on the product label */
    @Column(name = "raw_label", nullable = false, length = 200)
    private String rawLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status;

    /**
     * Outcome of the dynamic validation against product ingredients
     * and nutritional values. Embedded directly in this table.
     */
    @Embedded
    private ValidationResult validationResult;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;
}