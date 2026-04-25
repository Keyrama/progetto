package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.config.RoleGuard;
import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.service.ClaimDefinitionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Role matrix:
 *   GET              — open (all roles — consumers need to read the library)
 *   POST / PUT       — SPECIALIST, CORPORATE (experts manage the claim library)
 *   DELETE           — CORPORATE only
 */
@RestController
@RequestMapping("/api/claims/definitions")
@RequiredArgsConstructor
public class ClaimDefinitionController {

    private final ClaimDefinitionService claimDefinitionService;
    private final RoleGuard roleGuard;

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
    public ResponseEntity<ClaimDefinitionDTO> create(
            @Valid @RequestBody ClaimDefinitionDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.SPECIALIST, User.Role.CORPORATE);
        ClaimDefinitionDTO created = claimDefinitionService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDefinitionDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClaimDefinitionDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.SPECIALIST, User.Role.CORPORATE);
        return ResponseEntity.ok(claimDefinitionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        claimDefinitionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}