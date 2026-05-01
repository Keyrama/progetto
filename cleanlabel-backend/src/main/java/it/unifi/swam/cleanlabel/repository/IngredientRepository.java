package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>,
        JpaSpecificationExecutor<Ingredient> {
}