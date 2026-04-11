package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nutritional_claims")
public class NutritionalClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String label;

    @Column(name = "is_validated", nullable = false)
    private boolean isValidated = false;

    @Column(name = "is_misleading", nullable = false)
    private boolean isMisleading = false;

    @Column(length = 1000)
    private String explanation;

    @Column(name = "product_id")
    private Long productId;

    public NutritionalClaim() {}

    public NutritionalClaim(String label, boolean isValidated,
                            boolean isMisleading, String explanation) {
        this.label = label;
        this.isValidated = isValidated;
        this.isMisleading = isMisleading;
        this.explanation = explanation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public boolean isValidated() { return isValidated; }
    public void setValidated(boolean validated) { isValidated = validated; }
    public boolean isMisleading() { return isMisleading; }
    public void setMisleading(boolean misleading) { isMisleading = misleading; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}