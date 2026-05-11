package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

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
                    "I valori di fibre sono mancanti: non è possibile verificare la richiesta di fibre."
            );
        }

        double threshold = claim.getValidationThreshold() != null
                ? claim.getValidationThreshold()
                : DEFAULT_THRESHOLD;

        double fiber = nv.getFiber();

        if (fiber >= threshold) {
            return new ValidationResult(
                    ValidationResult.Verdict.CONFIRMED,
                    String.format("Fibre: %.2fg/100g — in linea con la soglia minima di %.2fg/100g richiesta da questa dichiarazione.",
                            fiber, threshold)
            );
        }

        return new ValidationResult(
                ValidationResult.Verdict.CONTRADICTED,
                String.format("Fibre: %.2fg/100g — sotto la soglia minima di %.2fg/100g " +
                        "richiesta da questa dichiarazione.", fiber, threshold)
        );
    }
}