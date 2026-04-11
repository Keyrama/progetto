package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an allergen that may be present in or contaminate a product.
 * Based on the 14 major allergens defined by EU Regulation No 1169/2011.
 */
@Entity
@Table(name = "allergens")
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Short official code (e.g., "GLUTEN", "MILK", "NUTS") */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 500)
    private String description;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Allergen() {}

    public Allergen(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
