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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 *
 * Pagination:
 *   GET /api/products?page=0&size=20&sort=name,asc
 *   Response body is a Spring Page<ProductDTO> with metadata:
 *     { content: [...], totalElements, totalPages, number, size }
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

    /**
     * @param page  0-based page index (default 0)
     * @param size  page size (default 20, max 100)
     * @param sort  field and direction, e.g. "name,asc" or "healthScore,desc" (default "name,asc")
     */
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Boolean cleanLabel,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {

        // Cap page size to avoid abusive requests
        int cappedSize = Math.min(size, 100);

        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].trim().equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, cappedSize, Sort.by(direction, sortField));

        return ResponseEntity.ok(productService.findAll(search, category, cleanLabel, pageable));
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