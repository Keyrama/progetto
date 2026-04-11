package it.unifi.swam.cleanlabel.dtos;

import java.util.List;

/**
 * Data Transfer Object for Product.
 * Used for both list views (summary) and detail views.
 */
public class ProductDTO {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private String imageUrl;
    private Integer healthScore;
    private Integer sustainabilityScore;
    private boolean isCleanLabel;
    private String categoryName;

    // Included in detail view only
    private NutritionalValueDTO nutritionalValue;
    private List<IngredientDTO> ingredients;
    private List<AllergenDTO> allergens;
    private List<NutritionalClaimDTO> claims;

    // ── Constructors ──────────────────────────────────────────────────────────

    public ProductDTO() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getHealthScore() { return healthScore; }
    public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }

    public Integer getSustainabilityScore() { return sustainabilityScore; }
    public void setSustainabilityScore(Integer sustainabilityScore) {
        this.sustainabilityScore = sustainabilityScore;
    }

    public boolean isCleanLabel() { return isCleanLabel; }
    public void setCleanLabel(boolean cleanLabel) { isCleanLabel = cleanLabel; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public NutritionalValueDTO getNutritionalValue() { return nutritionalValue; }
    public void setNutritionalValue(NutritionalValueDTO nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
    }

    public List<IngredientDTO> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDTO> ingredients) { this.ingredients = ingredients; }

    public List<AllergenDTO> getAllergens() { return allergens; }
    public void setAllergens(List<AllergenDTO> allergens) { this.allergens = allergens; }

    public List<NutritionalClaimDTO> getClaims() { return claims; }
    public void setClaims(List<NutritionalClaimDTO> claims) { this.claims = claims; }
}
