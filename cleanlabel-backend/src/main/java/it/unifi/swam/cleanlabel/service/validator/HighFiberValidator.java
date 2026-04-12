package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

/**
 * Validates claims like "high fibre" and "source of fibre".
 * Checks product.nutritionalValue.fiber against the claim's validation threshold.
 *
 * EU thresholds (Reg. 1924/2006):
 *   "source of fibre"  → fiber >= 3g/100g
 *   "high fibre"       → fiber >= 6g/100g
 */
@Component
public class HighFiberValidator implements ClaimValidatorStrategy {

    private static final double DEFAULT_THRESHOLD = 6.0;

    @Override
    public ClaimDefinition.ValidationStrategy supports() {
        return ClaimDefinition.ValidationStrategy.HIGH_FIBER;
    }

    @Override
    public ValidationResult validate(Product product, ClaimDefinition claim) {
        NutritionalValue nv = product.getNutritionalValue();

        if (nv == null || nv.getFiber() == null) {
            return new ValidationResult(
                    ValidationResult.Verdict.INCOMPLETE_DATA,
                    "Fiber value is not declared: cannot verify the fibre claim."
            );
        }

        double threshold = claim.getValidationThreshold() != null
                ? claim.getValidationThreshold()
                : DEFAULT_THRESHOLD;

        double fiber = nv.getFiber();

        if (fiber >= threshold) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    String.format("Fibre: %.2fg/100g — meets the minimum threshold of %.2fg/100g.",
                            fiber, threshold)
            );
        }

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                String.format("Fibre: %.2fg/100g — below the minimum threshold of %.2fg/100g " +
                        "required by this claim.", fiber, threshold)
        );
    }
}