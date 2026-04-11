package it.unifi.swam.cleanlabel.dtos;

/**
 * DTO returned by GET /api/products/{id}/alternatives
 */
public class AlternativeSuggestionDTO {

    private Long id;
    private ProductDTO targetProduct;
    private String reason;
    private Integer scoreDelta;

    public AlternativeSuggestionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductDTO getTargetProduct() { return targetProduct; }
    public void setTargetProduct(ProductDTO targetProduct) { this.targetProduct = targetProduct; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getScoreDelta() { return scoreDelta; }
    public void setScoreDelta(Integer scoreDelta) { this.scoreDelta = scoreDelta; }
}
