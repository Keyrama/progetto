package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class NoValidationStrategy implements ClaimValidatorStrategy {

    @Override
    public ClaimDefinition.ValidationStrategy supports() {
        return ClaimDefinition.ValidationStrategy.NONE;
    }

    @Override
    public ValidationResult validate(Product product, ClaimDefinition claim) {
        return new ValidationResult(
                ValidationResult.Verdict.UNVERIFIABLE,
                "Questa è una dichiarazione soggettiva o di marketing senza definizione legale. " +
                "La validazione automatica non è applicabile. È consigliata una revisione da parte di uno specialista."
        );
    }
}