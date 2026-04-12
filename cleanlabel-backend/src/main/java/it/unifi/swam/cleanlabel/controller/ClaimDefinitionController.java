package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.service.ClaimDefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for the ClaimDefinition master library.
 *
 * Base path: /api/claims/definitions
 *
 * The library is seeded at boot via data.sql.
 * GET endpoints are public (used by the FE to decode labels).
 * POST/PUT/DELETE are for SPECIALIST/CORPORATE roles only (mocked).
 */
@RestController
@RequestMapping("/api/claims/definitions")
@RequiredArgsConstructor
public class ClaimDefinitionController {

    private final ClaimDefinitionService claimDefinitionService;

    // ── GET /api/claims/definitions ────────────────────────────────────────────

    /**
     * Returns all claim definitions.
     * Optional filter:
     *   ?misleading=true     — only inherently misleading claims
     *   ?type=MARKETING      — filter by claim type (NUTRITIONAL, HEALTH, MARKETING)
     */
    @GetMapping
    public ResponseEntity<List<ClaimDefinitionDTO>> getAll(
            @RequestParam(required = false) Boolean misleading,
            @RequestParam(required = false) String type) {

        return ResponseEntity.ok(claimDefinitionService.findAll(misleading, type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDefinitionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(claimDefinitionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClaimDefinitionDTO> create(@Valid @RequestBody ClaimDefinitionDTO dto) {
        ClaimDefinitionDTO created = claimDefinitionService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDefinitionDTO> update(
            @PathVariable Long id, @Valid @RequestBody ClaimDefinitionDTO dto) {
        return ResponseEntity.ok(claimDefinitionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        claimDefinitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}