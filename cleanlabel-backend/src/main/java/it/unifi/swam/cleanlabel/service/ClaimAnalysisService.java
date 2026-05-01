package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ProductClaimDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ProductClaimMapper;
import it.unifi.swam.cleanlabel.model.*;
import it.unifi.swam.cleanlabel.repository.ClaimDefinitionRepository;
import it.unifi.swam.cleanlabel.repository.ProductClaimRepository;
import it.unifi.swam.cleanlabel.repository.ProductRepository;
import it.unifi.swam.cleanlabel.service.validator.ClaimValidatorStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Orchestrates the two-phase claim analysis process:
 *
 * Phase 1 — Matching:
 *   Each raw claim string (from the product label) is looked up in the
 *   ClaimDefinition master library. If found (MATCHED), the definition tells us
 *   whether the claim is regulated, misleading by definition, and which
 *   validation strategy to apply.
 *
 * Phase 2 — Dynamic validation:
 *   The appropriate ClaimValidatorStrategy implementation is selected based on
 *   ClaimDefinition.validationStrategy and run against the product's actual
 *   ingredients and nutritional values. The result (CONFIRMED / CONTRADICTED /
 *   UNVERIFIABLE / INCOMPLETE_DATA) is stored in the embedded ValidationResult.
 *
 * Results are persisted as ProductClaim entities and replace any previous
 * analysis for the same product. Claims are NOT navigated through Product —
 * they are managed entirely through ProductClaimRepository.
 */
@Service
@Transactional(readOnly = true)
public class ClaimAnalysisService {

    private final ClaimDefinitionRepository claimDefinitionRepository;
    private final ProductClaimRepository productClaimRepository;
    private final ProductRepository productRepository;
    private final ProductClaimMapper productClaimMapper;

    /**
     * Map from ValidationStrategy enum → concrete validator bean.
     * Spring injects all ClaimValidatorStrategy implementations and we index
     * them by the strategy they support for O(1) lookup at runtime.
     */
    private final Map<ClaimDefinition.ValidationStrategy, ClaimValidatorStrategy> validators;

    public ClaimAnalysisService(
            ClaimDefinitionRepository claimDefinitionRepository,
            ProductClaimRepository productClaimRepository,
            ProductRepository productRepository,
            ProductClaimMapper productClaimMapper,
            List<ClaimValidatorStrategy> validatorList) {

        this.claimDefinitionRepository = claimDefinitionRepository;
        this.productClaimRepository    = productClaimRepository;
        this.productRepository         = productRepository;
        this.productClaimMapper        = productClaimMapper;
        this.validators = validatorList.stream()
                .collect(Collectors.toMap(
                        ClaimValidatorStrategy::supports,
                        Function.identity()
                ));
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Analyzes a list of raw claim strings for a product.
     * Duplicates in the input are silently removed to avoid unique constraint
     * violations on (product_id, claim_definition_id).
     * Any previous analysis for the same product is replaced entirely.
     *
     * @param productId ID of the product whose label claims are being analyzed
     * @param rawClaims list of claim strings exactly as they appear on the label
     * @return          list of ProductClaimDTO with full analysis results
     */
    @Transactional
    public List<ProductClaimDTO> analyzeClaims(Long productId, List<String> rawClaims) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // Remove previous analysis — analysis is always a full replacement.
        // Done via repository, not through product.getClaims(), since the
        // OneToMany is intentionally absent from Product.
        productClaimRepository.deleteByProductId(productId);

        // Deduplicate to avoid unique constraint violations on
        // (product_id, claim_definition_id) when the same term appears twice.
        List<String> deduplicated = rawClaims.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();

        List<ProductClaim> results = deduplicated.stream()
                .map(raw -> buildProductClaim(product, raw))
                .toList();

        productClaimRepository.saveAll(results);

        return productClaimMapper.toDTOList(results);
    }

    /**
     * Returns all persisted claim analysis results for a product.
     */
    public List<ProductClaimDTO> getClaimsByProduct(Long productId) {
        ensureProductExists(productId);
        return productClaimMapper.toDTOList(
                productClaimRepository.findByProductId(productId));
    }

    /**
     * Returns only the claims flagged as misleading in the library.
     * A claim is misleading when its ClaimDefinition has misleading=true,
     * regardless of the dynamic validation verdict.
     */
    public List<ProductClaimDTO> getMisleadingClaims(Long productId) {
        ensureProductExists(productId);
        return productClaimMapper.toDTOList(
                productClaimRepository.findMisleadingByProductId(productId));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private ProductClaim buildProductClaim(Product product, String rawLabel) {
        ProductClaim pc = new ProductClaim();
        pc.setProduct(product);
        pc.setRawLabel(rawLabel);
        pc.setAnalyzedAt(LocalDateTime.now());

        Optional<ClaimDefinition> definition =
                claimDefinitionRepository.findByTermIgnoreCase(rawLabel);

        definition.ifPresentOrElse(
                def -> {
                    // Phase 1: claim found in library
                    pc.setClaimDefinition(def);
                    pc.setStatus(ProductClaim.AnalysisStatus.MATCHED);

                    // Phase 2: dynamic validation against product data
                    ClaimValidatorStrategy validator = resolveValidator(def);
                    pc.setValidationResult(validator.validate(product, def));
                },
                () -> {
                    // Claim not in library — flag for manual specialist review
                    pc.setStatus(ProductClaim.AnalysisStatus.UNMATCHED);
                    pc.setValidationResult(new ValidationResult(
                            ValidationResult.Verdict.UNVERIFIABLE,
                            "Claim non trovato nella libreria. Richiede revisione manuale da parte di uno specialista."
                    ));
                }
        );

        return pc;
    }

    /**
     * Resolves the correct validator for a claim definition.
     * Falls back to NONE strategy if no specific validator is registered,
     * which should never happen if all enum values have a corresponding bean.
     */
    private ClaimValidatorStrategy resolveValidator(ClaimDefinition def) {
        return validators.getOrDefault(
                def.getValidationStrategy(),
                validators.get(ClaimDefinition.ValidationStrategy.NONE)
        );
    }

    private void ensureProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", productId);
        }
    }
}