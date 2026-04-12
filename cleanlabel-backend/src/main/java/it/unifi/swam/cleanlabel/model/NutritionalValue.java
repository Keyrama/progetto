package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Nutritional values per 100g/100ml of product.
 * Owned exclusively by one Product (OneToOne).
 */
@Getter
@Setter
@NoArgsConstructor
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

    /** Nullable: not all products declare fiber content */
    @Column
    private Double fiber;
}