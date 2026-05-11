package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_claims")
public class ProductClaim {

    public enum AnalysisStatus {
        MATCHED,
        UNMATCHED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_definition_id")
    private ClaimDefinition claimDefinition;

    @Column(name = "raw_label", nullable = false, length = 200)
    private String rawLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status;

    @Embedded
    private ValidationResult validationResult;

    @Column(name = "analyzed_at", nullable = false)
    private LocalDateTime analyzedAt;
}