package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.config.RoleGuard;
import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.service.IngredientService;
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
 *   POST / PUT       — SPECIALIST, CORPORATE
 *   DELETE           — CORPORATE only
 */
@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final RoleGuard roleGuard;

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAll(
            @RequestParam(required = false) Boolean artificial,
            @RequestParam(required = false) String riskLevel) {
        return ResponseEntity.ok(ingredientService.findAll(artificial, riskLevel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.findById(id));
    }

    @PostMapping
    public ResponseEntity<IngredientDTO> create(
            @Valid @RequestBody IngredientDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.SPECIALIST, User.Role.CORPORATE);
        IngredientDTO created = ingredientService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody IngredientDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.SPECIALIST, User.Role.CORPORATE);
        return ResponseEntity.ok(ingredientService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}