package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a healthier alternative product suggestion.
 * Links a source product (lower score) to a target product (higher score)
 * within the same category, with a human-readable reason.
 */
@Setter
@Getter
@Entity
@Table(name = "alternative_suggestions")
public class AlternativeSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_product_id", nullable = false)
    private Product sourceProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_product_id", nullable = false)
    private Product targetProduct;

    /** Explanation of why the target product is a better choice */
    @Column(length = 500)
    private String reason;

    /**
     * Score delta: how much better the target product scores
     * compared to the source product (positive value)
     */
    @Column(name = "score_delta")
    private Integer scoreDelta;

}
