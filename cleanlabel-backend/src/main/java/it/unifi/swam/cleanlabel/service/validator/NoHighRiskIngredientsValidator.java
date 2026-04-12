package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.Ingredient;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates claims like "no high risk additives".
 * Checks that none of the product's ingredients have RiskLevel.HIGH.
 */
@Component
public class NoHighRiskIngredientsValidator implements ClaimValidatorStrategy {

    @Override
    public ClaimDefinition.ValidationStrategy supports() {
        return ClaimDefinition.ValidationStrategy.NO_HIGH_RISK_INGREDIENTS;
    }

    @Override
    public ValidationResult validate(Product product, ClaimDefinition claim) {
        List<Ingredient> highRisk = product.getIngredients().stream()
                .filter(i -> i.getRiskLevel() == Ingredient.RiskLevel.HIGH)
                .toList();

        if (highRisk.isEmpty()) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    "No HIGH-risk ingredients detected in the ingredient list."
            );
        }

        String names = highRisk.stream()
                .map(i -> i.getENumber() != null
                        ? i.getName() + " (" + i.getENumber() + ")"
                        : i.getName())
                .collect(Collectors.joining(", "));

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                "The product contains HIGH-risk ingredients: " + names + "."
        );
    }
}