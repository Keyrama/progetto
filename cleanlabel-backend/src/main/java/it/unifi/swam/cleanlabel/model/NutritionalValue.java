package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nutritional_values")
public class NutritionalValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double calories;

    @Column(nullable = false)
    private Double proteins;

    @Column(nullable = false)
    private Double carbohydrates;

    @Column(nullable = false)
    private Double sugars;

    @Column(nullable = false)
    private Double fats;

    @Column(name = "saturated_fats", nullable = false)
    private Double saturatedFats;

    @Column(nullable = false)
    private Double salt;

    @Column(nullable = true)
    private Double fiber;

    public NutritionalValue() {}

    public NutritionalValue(Double calories, Double proteins, Double carbohydrates,
                            Double sugars, Double fats, Double saturatedFats,
                            Double salt, Double fiber) {
        this.calories = calories;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.sugars = sugars;
        this.fats = fats;
        this.saturatedFats = saturatedFats;
        this.salt = salt;
        this.fiber = fiber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }
    public Double getProteins() { return proteins; }
    public void setProteins(Double proteins) { this.proteins = proteins; }
    public Double getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(Double carbohydrates) { this.carbohydrates = carbohydrates; }
    public Double getSugars() { return sugars; }
    public void setSugars(Double sugars) { this.sugars = sugars; }
    public Double getFats() { return fats; }
    public void setFats(Double fats) { this.fats = fats; }
    public Double getSaturatedFats() { return saturatedFats; }
    public void setSaturatedFats(Double saturatedFats) { this.saturatedFats = saturatedFats; }
    public Double getSalt() { return salt; }
    public void setSalt(Double salt) { this.salt = salt; }
    public Double getFiber() { return fiber; }
    public void setFiber(Double fiber) { this.fiber = fiber; }
}