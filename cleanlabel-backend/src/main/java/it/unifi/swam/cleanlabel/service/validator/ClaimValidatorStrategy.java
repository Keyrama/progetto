package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;

public interface ClaimValidatorStrategy {

    ClaimDefinition.ValidationStrategy supports();

    /**
     * Validates the claim against the product's actual data.
     *
     * @param product the product to validate against (with ingredients and
     *                nutritional values already loaded)
     * @param claim   the ClaimDefinition containing thresholds and metadata
     * @return        a ValidationResult with verdict and human-readable detail
     */
    ValidationResult validate(Product product, ClaimDefinition claim);
}