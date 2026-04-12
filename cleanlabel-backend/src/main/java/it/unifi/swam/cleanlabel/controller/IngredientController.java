package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for Ingredient resources.
 *
 * Base path: /api/ingredients
 *
 * Ingredients are shared entities referenced by products via IDs.
 * They carry allergen information and risk classification.
 */
@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    // ── GET /api/ingredients ───────────────────────────────────────────────────

    /**
     * Returns all ingredients.
     * Optional filters:
     *   ?artificial=true         — only artificial ingredients/additives
     *   ?riskLevel=HIGH          — filter by risk level (LOW, MEDIUM, HIGH)
     */
    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAll(
            @RequestParam(required = false) Boolean artificial,
            @RequestParam(required = false) String riskLevel) {

        return ResponseEntity.ok(ingredientService.findAll(artificial, riskLevel));
    }

    // ── GET /api/ingredients/{id} ──────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.findById(id));
    }

    // ── POST /api/ingredients ──────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<IngredientDTO> create(@Valid @RequestBody IngredientDTO dto) {
        IngredientDTO created = ingredientService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // ── PUT /api/ingredients/{id} ──────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody IngredientDTO dto) {
        return ResponseEntity.ok(ingredientService.update(id, dto));
    }

    // ── DELETE /api/ingredients/{id} ───────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}