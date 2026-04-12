package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.dtos.AlternativeSuggestionDTO;
import it.unifi.swam.cleanlabel.dtos.ClaimAnalysisRequestDTO;
import it.unifi.swam.cleanlabel.dtos.ProductClaimDTO;
import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.service.ClaimAnalysisService;
import it.unifi.swam.cleanlabel.service.AlternativeSuggestionService;
import it.unifi.swam.cleanlabel.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for Product resources.
 *
 * Base path: /api/products
 *
 * Use cases covered:
 *   UC1 — Browse and search the product catalogue
 *   UC2 — View full product detail (ingredients, allergens, nutritional values)
 *   UC3 — Analyze label claims (misleading claim detection + dynamic validation)
 *   UC4 — Get healthier product alternatives
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ClaimAnalysisService claimAnalysisService;
    private final AlternativeSuggestionService alternativeSuggestionService;

    // ── GET /api/products ──────────────────────────────────────────────────────

    /**
     * Returns the full product catalogue.
     * Optional query params for filtering:
     *   ?search=query     — name/brand full-text search
     *   ?category=id      — filter by category
     *   ?cleanLabel=true  — filter clean-label compliant products only
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Boolean cleanLabel) {

        List<ProductDTO> products = productService.findAll(search, category, cleanLabel);
        return ResponseEntity.ok(products);
    }

    // ── GET /api/products/{id} ─────────────────────────────────────────────────

    /**
     * Returns full product detail including ingredients, derived allergens,
     * nutritional values, and claim analysis results.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    // ── POST /api/products ─────────────────────────────────────────────────────

    /**
     * Creates a new product. The health score and cleanLabel flag are computed
     * automatically by the service from the provided data.
     */
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto) {
        ProductDTO created = productService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // ── PUT /api/products/{id} ─────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    // ── DELETE /api/products/{id} ──────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── POST /api/products/{id}/claims/analyze ─────────────────────────────────

    /**
     * UC3 — Analyze the label claims of a product.
     *
     * Accepts a list of raw claim strings as they appear on the product label.
     * For each claim:
     *   1. Matches against the ClaimDefinition library (known EU-regulated and
     *      marketing claims).
     *   2. Dynamically validates the claim against the product's actual
     *      ingredients and nutritional values.
     *
     * Returns a list of ProductClaimDTO with:
     *   - whether the claim is in the library (MATCHED / UNMATCHED)
     *   - whether it is inherently misleading (e.g. "natural" has no legal definition)
     *   - the dynamic validation verdict (CONFIRMED / CONTRADICTED / UNVERIFIABLE /
     *     INCOMPLETE_DATA)
     */
    @PostMapping("/{id}/claims/analyze")
    public ResponseEntity<List<ProductClaimDTO>> analyzeClaims(
            @PathVariable Long id,
            @Valid @RequestBody ClaimAnalysisRequestDTO request) {

        List<ProductClaimDTO> claims =
                claimAnalysisService.analyzeClaims(id, request.getRawClaims());
        return ResponseEntity.ok(claims);
    }

    // ── GET /api/products/{id}/claims ──────────────────────────────────────────

    /**
     * Returns the persisted claim analysis results for a product.
     * Optional filter:
     *   ?misleading=true  — returns only claims flagged as misleading in the library
     */
    @GetMapping("/{id}/claims")
    public ResponseEntity<List<ProductClaimDTO>> getClaims(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean misleading) {

        List<ProductClaimDTO> claims = misleading != null && misleading
                ? claimAnalysisService.getMisleadingClaims(id)
                : claimAnalysisService.getClaimsByProduct(id);
        return ResponseEntity.ok(claims);
    }

    // ── GET /api/products/{id}/alternatives ────────────────────────────────────

    /**
     * UC4 — Suggests healthier product alternatives.
     *
     * Returns products in the same category with a higher health score,
     * ordered by score descending. Does not persist results — computed at runtime.
     *
     * @param limit max number of alternatives to return (default 5)
     */
    @GetMapping("/{id}/alternatives")
    public ResponseEntity<List<AlternativeSuggestionDTO>> getAlternatives(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(
                alternativeSuggestionService.findAlternatives(id, limit));
    }
}