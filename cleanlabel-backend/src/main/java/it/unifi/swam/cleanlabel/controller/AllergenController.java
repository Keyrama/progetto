package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.config.RoleGuard;
import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.service.AllergenService;
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
 *   GET              — open (all roles)
 *   POST / PUT       — CORPORATE only (allergen master data is company-managed)
 *   DELETE           — CORPORATE only
 */
@RestController
@RequestMapping("/api/allergens")
@RequiredArgsConstructor
public class AllergenController {

    private final AllergenService allergenService;
    private final RoleGuard roleGuard;

    @GetMapping
    public ResponseEntity<List<AllergenDTO>> getAll() {
        return ResponseEntity.ok(allergenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AllergenDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(allergenService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AllergenDTO> create(
            @Valid @RequestBody AllergenDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        AllergenDTO created = allergenService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AllergenDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AllergenDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        return ResponseEntity.ok(allergenService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        allergenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}