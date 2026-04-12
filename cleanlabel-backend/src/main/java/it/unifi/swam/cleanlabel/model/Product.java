package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Central domain entity. Represents a packaged food or beverage product
 * in the company catalogue with its full metadata.
 */
@Getter
@Setter
@NoArgsConstructor
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

    /**
     * Health score 0–100. Computed by HealthScoreService
     * based on nutritional values, artificial ingredients, and risk levels.
     * Stored for caching — recomputed on product update.
     */
    @Column(name = "health_score")
    private Integer healthScore;

    /**
     * Sustainability score 0–100.
     * Based on packaging, carbon footprint, and sourcing indicators.
     */
    @Column(name = "sustainability_score")
    private Integer sustainabilityScore;

    /** True if the product complies with clean-label paradigm criteria */
    @Column(name = "is_clean_label", nullable = false)
    private boolean cleanLabel = false;

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

    /**
     * "May contain traces of..." — explicitly declared cross-contamination.
     * Distinct from allergens derived from ingredients: here the product declares
     * potential contamination even if the allergen is not in any ingredient.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_may_contain_allergens",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> mayContainAllergens = new ArrayList<>();

    /**
     * Claims found on this product's label.
     * Populated by ClaimAnalysisService — not set directly.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductClaim> claims = new ArrayList<>();

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Derives allergens from the ingredient list.
     * Not persisted — computed at runtime from ingredient relationships.
     * Use this in DTOs and services to expose the full allergen picture.
     */
    @Transient
    public List<Allergen> getDeclaredAllergens() {
        return ingredients.stream()
                .flatMap(i -> i.getAllergens().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void addMayContainAllergen(Allergen allergen) {
        this.mayContainAllergens.add(allergen);
    }
}