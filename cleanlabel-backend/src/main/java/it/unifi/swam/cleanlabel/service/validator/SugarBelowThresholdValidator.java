package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

/**
 * Validates claims like "sugar free", "no added sugars", "low sugar".
 * Checks product.nutritionalValue.sugars against the claim's validation threshold.
 * Threshold unit: g per 100g product (as per EU Reg. 1924/2006).
 */
@Component
public class SugarBelowThresholdValidator implements ClaimValidatorStrategy {

    /** EU default for "sugar free": ≤ 0.5g/100g */
    private static final double DEFAULT_THRESHOLD = 0.5;

    @Override
    public ClaimDefinition.ValidationStrategy supports() {
        return ClaimDefinition.ValidationStrategy.SUGAR_BELOW_THRESHOLD;
    }

    @Override
    public ValidationResult validate(Product product, ClaimDefinition claim) {
        NutritionalValue nv = product.getNutritionalValue();

        if (nv == null || nv.getSugars() == null) {
            return new ValidationResult(
                    ValidationResult.Verdict.INCOMPLETE_DATA,
                    "Nutritional values are missing: cannot verify sugar content."
            );
        }

        double threshold = claim.getValidationThreshold() != null
                ? claim.getValidationThreshold()
                : DEFAULT_THRESHOLD;

        double sugars = nv.getSugars();

        if (sugars <= threshold) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    String.format("Sugars: %.2fg/100g — within the allowed threshold of %.2fg/100g.",
                            sugars, threshold)
            );
        }

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                String.format("Sugars: %.2fg/100g — exceeds the threshold of %.2fg/100g " +
                        "required by this claim.", sugars, threshold)
        );
    }
}