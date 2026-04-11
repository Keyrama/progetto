package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single ingredient or food additive.
 * E-numbers refer to the European food additive numbering system.
 */
@Entity
@Table(name = "ingredients")
public class Ingredient {

    public enum RiskLevel { LOW, MEDIUM, HIGH }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    /** E-number if this is a food additive (e.g., "E621" for MSG). Null for natural ingredients. */
    @Column(name = "e_number", length = 10)
    private String eNumber;

    /** Plain-language explanation of what this ingredient is and its role in food. */
    @Column(length = 1000)
    private String description;

    @Column(name = "is_artificial", nullable = false)
    private boolean isArtificial = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel = RiskLevel.LOW;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Ingredient() {}

    public Ingredient(String name, String eNumber, String description,
                      boolean isArtificial, RiskLevel riskLevel) {
        this.name = name;
        this.eNumber = eNumber;
        this.description = description;
        this.isArtificial = isArtificial;
        this.riskLevel = riskLevel;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getENumber() { return eNumber; }
    public void setENumber(String eNumber) { this.eNumber = eNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isArtificial() { return isArtificial; }
    public void setArtificial(boolean artificial) { isArtificial = artificial; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

}
