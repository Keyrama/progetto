package it.unifi.swam.cleanlabel.repository.spec;

import it.unifi.swam.cleanlabel.model.Ingredient;
import org.springframework.data.jpa.domain.Specification;

public class IngredientSpecifications {

    private IngredientSpecifications() {}

    public static Specification<Ingredient> isArtificial() {
        return (root, cq, cb) -> cb.isTrue(root.get("artificial"));
    }

    public static Specification<Ingredient> hasRiskLevel(Ingredient.RiskLevel level) {
        return (root, cq, cb) -> cb.equal(root.get("riskLevel"), level);
    }
}