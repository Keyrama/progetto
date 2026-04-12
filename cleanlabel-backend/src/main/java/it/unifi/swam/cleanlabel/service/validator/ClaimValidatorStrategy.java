package it.unifi.swam.cleanlabel.service.validator;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ValidationResult;

/**
 * Strategy interface for dynamic claim validation.
 *
 * Each implementation validates one specific ValidationStrategy enum value
 * by inspecting the product's actual data (ingredients, nutritional values).
 *
 * New strategies can be added without modifying ClaimAnalysisService:
 * Spring auto-discovers all beans implementing this interface.
 */
public interface ClaimValidatorStrategy {

    /** Returns the ValidationStrategy this implementation handles. */
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