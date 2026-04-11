package it.unifi.swam.cleanlabel.dtos;

import java.util.List;

/**
 * DTO returned by GET /api/products/{id}/score
 * Contains the overall health score and a detailed breakdown.
 */
public class HealthScoreDTO {

    private Long productId;
    private String productName;

    /** Overall health score 0-100 */
    private Integer totalScore;

    /** Sub-score based on nutritional values (0-40 points) */
    private Integer nutritionScore;

    /** Sub-score based on ingredient quality (0-40 points) */
    private Integer ingredientScore;

    /** Sub-score based on absence of misleading claims (0-20 points) */
    private Integer claimScore;

    /** Whether the product qualifies as clean-label */
    private boolean isCleanLabel;

    /** List of flagged ingredients with HIGH or MEDIUM risk */
    private List<IngredientDTO> flaggedIngredients;

    /** List of misleading claims identified on the label */
    private List<NutritionalClaimDTO> misleadingClaims;

    /** Short human-readable summary of the score */
    private String summary;

    public HealthScoreDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }

    public Integer getNutritionScore() { return nutritionScore; }
    public void setNutritionScore(Integer nutritionScore) { this.nutritionScore = nutritionScore; }

    public Integer getIngredientScore() { return ingredientScore; }
    public void setIngredientScore(Integer ingredientScore) {
        this.ingredientScore = ingredientScore;
    }

    public Integer getClaimScore() { return claimScore; }
    public void setClaimScore(Integer claimScore) { this.claimScore = claimScore; }

    public boolean isCleanLabel() { return isCleanLabel; }
    public void setCleanLabel(boolean cleanLabel) { isCleanLabel = cleanLabel; }

    public List<IngredientDTO> getFlaggedIngredients() { return flaggedIngredients; }
    public void setFlaggedIngredients(List<IngredientDTO> flaggedIngredients) {
        this.flaggedIngredients = flaggedIngredients;
    }

    public List<NutritionalClaimDTO> getMisleadingClaims() { return misleadingClaims; }
    public void setMisleadingClaims(List<NutritionalClaimDTO> misleadingClaims) {
        this.misleadingClaims = misleadingClaims;
    }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
