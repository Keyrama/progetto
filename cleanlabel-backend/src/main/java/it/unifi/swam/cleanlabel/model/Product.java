package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(name = "health_score")
    private Integer healthScore;

    @Column(name = "sustainability_score")
    private Integer sustainabilityScore;

    @Column(name = "is_clean_label", nullable = false)
    private boolean cleanLabel = false;


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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_may_contain_allergens",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> mayContainAllergens = new ArrayList<>();

    @Transient
    public List<Allergen> getDeclaredAllergens() {
        return ingredients.stream()
                .flatMap(i -> i.getAllergens().stream())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

}