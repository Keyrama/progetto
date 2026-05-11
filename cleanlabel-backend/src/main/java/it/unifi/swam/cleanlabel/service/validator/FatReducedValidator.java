package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

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
                    "I valori di grassi sono mancanti: non è possibile verificare la richiesta di grassi."
            );
        }

        double threshold = claim.getValidationThreshold() != null
                ? claim.getValidationThreshold()
                : DEFAULT_THRESHOLD;

        double fats = nv.getFats();

        if (fats <= threshold) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    String.format("Grassi totali: %.2fg/100g — entro la soglia consentita di %.2fg/100g.",
                            fats, threshold)
            );
        }

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                String.format("Grassi totali: %.2fg/100g — supera la soglia di %.2fg/100g " +
                        "richiesta da questa affermazione.", fats, threshold)
        );
    }
}