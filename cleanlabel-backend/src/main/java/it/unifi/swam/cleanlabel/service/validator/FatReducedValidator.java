package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

/**
 * Validates fat-related claims such as "low fat" or "reduced fat".
 *
 * EU thresholds (Reg. 1924/2006):
 *   "low fat"      → fats <= 3g/100g for solids (default threshold)
 *   "fat free"     → fats <= 0.5g/100g
 *
 * The exact threshold is taken from the ClaimDefinition.validationThreshold field,
 * so different claims can share this validator with different cutoffs.
 */
@Component
public class FatReducedValidator implements ClaimValidatorStrategy {

    private static final double DEFAULT_THRESHOLD = 3.0;

    @Override
    public ClaimDefinition.ValidationStrategy supports() {
        return ClaimDefinition.ValidationStrategy.FAT_REDUCED;
    }

    @Override
    public ValidationResult validate(Product product, ClaimDefinition claim) {
        NutritionalValue nv = product.getNutritionalValue();

        if (nv == null || nv.getFats() == null) {
            return new ValidationResult(
                    ValidationResult.Verdict.INCOMPLETE_DATA,
                    "Fat values are missing: cannot verify the fat claim."
            );
        }

        double threshold = claim.getValidationThreshold() != null
                ? claim.getValidationThreshold()
                : DEFAULT_THRESHOLD;

        double fats = nv.getFats();

        if (fats <= threshold) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    String.format("Total fat: %.2fg/100g — within the allowed threshold of %.2fg/100g.",
                            fats, threshold)
            );
        }

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                String.format("Total fat: %.2fg/100g — exceeds the threshold of %.2fg/100g " +
                        "required by this claim.", fats, threshold)
        );
    }
}