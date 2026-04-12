package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Master entity for the 14 major allergens defined by EU Regulation No 1169/2011.
 * Populated via data.sql at boot. Not user-editable.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "allergens")
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Official short code. E.g.: "GLUTEN", "MILK", "NUTS" */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 500)
    private String description;
}