package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Central domain entity. Represents a packaged food or beverage product
 * in the company catalogue with its full metadata.
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String brand;

    @Column(length = 1000)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * Health score from 0 to 100.
     * Computed based on nutritional values, number of artificial ingredients,
     * and presence of high-risk additives. Stored for caching purposes.
     */
    @Column(name = "health_score")
    private Integer healthScore;

    /**
     * Sustainability score from 0 to 100.
     * Based on packaging, carbon footprint, and sourcing indicators.
     */
    @Column(name = "sustainability_score")
    private Integer sustainabilityScore;

    /** True if the product complies with clean-label paradigm criteria */
    @Column(name = "is_clean_label", nullable = false)
    private boolean isCleanLabel = false;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "nutritional_value_id", referencedColumnName = "id")
    private NutritionalValue nutritionalValue;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "product_ingredients",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "product_allergens",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> allergens = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<NutritionalClaim> claims = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────

    public Product() {}

    public Product(String name, String brand, String description,
                   ProductCategory category, boolean isCleanLabel) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.isCleanLabel = isCleanLabel;
    }

    // ── Helper methods ────────────────────────────────────────────────────────

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void addAllergen(Allergen allergen) {
        this.allergens.add(allergen);
    }

    public void addClaim(NutritionalClaim claim) {
        this.claims.add(claim);
    }

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

    public ProductCategory getCategory() { return category; }
    public void setCategory(ProductCategory category) { this.category = category; }

    public NutritionalValue getNutritionalValue() { return nutritionalValue; }
    public void setNutritionalValue(NutritionalValue nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
    }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<Allergen> getAllergens() { return allergens; }
    public void setAllergens(List<Allergen> allergens) { this.allergens = allergens; }

    public List<NutritionalClaim> getClaims() { return claims; }
    public void setClaims(List<NutritionalClaim> claims) { this.claims = claims; }

}
