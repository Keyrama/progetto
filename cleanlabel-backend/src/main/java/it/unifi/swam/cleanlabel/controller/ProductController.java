package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.config.RoleGuard;
import it.unifi.swam.cleanlabel.dtos.AlternativeSuggestionDTO;
import it.unifi.swam.cleanlabel.dtos.ClaimAnalysisRequestDTO;
import it.unifi.swam.cleanlabel.dtos.ProductClaimDTO;
import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.service.AlternativeSuggestionService;
import it.unifi.swam.cleanlabel.service.ClaimAnalysisService;
import it.unifi.swam.cleanlabel.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
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
 * Role matrix:
 *   GET  /api/products              — open (all roles)
 *   GET  /api/products/{id}         — open (all roles)
 *   POST /api/products              — CORPORATE only
 *   PUT  /api/products/{id}         — CORPORATE only
 *   DELETE /api/products/{id}       — CORPORATE only
 *   POST .../claims/analyze         — SPECIALIST, CORPORATE
 *   GET  .../claims                 — open (all roles)
 *   GET  .../alternatives           — open (all roles)
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ClaimAnalysisService claimAnalysisService;
    private final AlternativeSuggestionService alternativeSuggestionService;
    private final RoleGuard roleGuard;

    // ── Open endpoints (all roles) ─────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Boolean cleanLabel) {

        return ResponseEntity.ok(productService.findAll(search, category, cleanLabel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/{id}/claims")
    public ResponseEntity<List<ProductClaimDTO>> getClaims(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean misleading) {

        List<ProductClaimDTO> claims = Boolean.TRUE.equals(misleading)
                ? claimAnalysisService.getMisleadingClaims(id)
                : claimAnalysisService.getClaimsByProduct(id);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/{id}/alternatives")
    public ResponseEntity<List<AlternativeSuggestionDTO>> getAlternatives(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(alternativeSuggestionService.findAlternatives(id, limit));
    }

    // ── CORPORATE only ─────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ProductDTO> create(
            @Valid @RequestBody ProductDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        ProductDTO created = productService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── SPECIALIST + CORPORATE ─────────────────────────────────────────────────

    @PostMapping("/{id}/claims/analyze")
    public ResponseEntity<List<ProductClaimDTO>> analyzeClaims(
            @PathVariable Long id,
            @Valid @RequestBody ClaimAnalysisRequestDTO request,
            HttpServletRequest httpRequest) {

        roleGuard.require(httpRequest, User.Role.SPECIALIST, User.Role.CORPORATE);
        return ResponseEntity.ok(
                claimAnalysisService.analyzeClaims(id, request.getRawClaims()));
    }
}