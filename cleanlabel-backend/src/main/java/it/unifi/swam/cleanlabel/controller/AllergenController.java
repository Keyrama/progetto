package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.service.AllergenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for Allergen resources.
 *
 * Base path: /api/allergens
 *
 * The 14 major EU allergens are seeded at boot.
 * POST/PUT/DELETE are provided for CORPORATE/SPECIALIST roles
 * (access control is mocked — no actual auth enforcement in prototype).
 */
@RestController
@RequestMapping("/api/allergens")
@RequiredArgsConstructor
public class AllergenController {

    private final AllergenService allergenService;

    @GetMapping
    public ResponseEntity<List<AllergenDTO>> getAll() {
        return ResponseEntity.ok(allergenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AllergenDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(allergenService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AllergenDTO> create(@Valid @RequestBody AllergenDTO dto) {
        AllergenDTO created = allergenService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AllergenDTO> update(
            @PathVariable Long id, @Valid @RequestBody AllergenDTO dto) {
        return ResponseEntity.ok(allergenService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        allergenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}