package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.Ingredient;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates claims like "no artificial ingredients" or "no artificial additives".
 * Checks that none of the product's ingredients are flagged as artificial.
 */
@Component
public class NoArtificialIngredientsValidator implements ClaimValidatorStrategy {

    @Override
    public ClaimDefinition.ValidationStrategy supports() {
        return ClaimDefinition.ValidationStrategy.NO_ARTIFICIAL_INGREDIENTS;
    }

    @Override
    public ValidationResult validate(Product product, ClaimDefinition claim) {
        List<Ingredient> artificial = product.getIngredients().stream()
                .filter(Ingredient::isArtificial)
                .toList();

        if (artificial.isEmpty()) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    "Nessun ingrediente artificiale rilevato nella lista degli ingredienti."
            );
        }

        String names = artificial.stream()
                .map(i -> i.getAdditiveCode() != null
                        ? i.getName() + " (" + i.getAdditiveCode() + ")"
                        : i.getName())
                .collect(Collectors.joining(", "));

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                "Il prodotto contiene ingredienti artificiali: " + names + "."
        );
    }
}