package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;

/**
 * Represents a healthier alternative product suggestion.
 * Links a source product (lower score) to a target product (higher score)
 * within the same category, with a human-readable reason.
 */
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

    // ── Constructors ──────────────────────────────────────────────────────────

    public AlternativeSuggestion() {}

    public AlternativeSuggestion(Product sourceProduct, Product targetProduct,
                                  String reason, Integer scoreDelta) {
        this.sourceProduct = sourceProduct;
        this.targetProduct = targetProduct;
        this.reason = reason;
        this.scoreDelta = scoreDelta;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getSourceProduct() { return sourceProduct; }
    public void setSourceProduct(Product sourceProduct) { this.sourceProduct = sourceProduct; }

    public Product getTargetProduct() { return targetProduct; }
    public void setTargetProduct(Product targetProduct) { this.targetProduct = targetProduct; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getScoreDelta() { return scoreDelta; }
    public void setScoreDelta(Integer scoreDelta) { this.scoreDelta = scoreDelta; }
}
