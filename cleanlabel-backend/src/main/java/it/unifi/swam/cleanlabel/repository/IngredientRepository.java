package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByArtificialTrue();
    List<Ingredient> findByRiskLevel(Ingredient.RiskLevel riskLevel);
}