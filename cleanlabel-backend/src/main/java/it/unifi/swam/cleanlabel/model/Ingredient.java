package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single ingredient or food additive.
 * Allergens are declared at ingredient level (e.g. "wheat flour" contains GLUTEN),
 * which allows the system to derive product allergens automatically.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
public class Ingredient {

    public enum RiskLevel { LOW, MEDIUM, HIGH }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    /**
     * additiveCode (E-number) if this is a food additive (e.g. "E621" for MSG).
     * Null for natural ingredients.
     */
    @Column(name = "additive_code", length = 10)
    private String additiveCode;

    /** Plain-language explanation of what this ingredient is and its role in food. */
    @Column(length = 1000)
    private String description;

    @Column(name = "is_artificial", nullable = false)
    private boolean artificial = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel = RiskLevel.LOW;

    /**
     * Allergens contained in this ingredient.
     * E.g. "wheat flour" → [GLUTEN].
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ingredient_allergens",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> allergens = new ArrayList<>();
}