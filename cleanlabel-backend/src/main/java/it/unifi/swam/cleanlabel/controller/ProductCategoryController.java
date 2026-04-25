package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.config.RoleGuard;
import it.unifi.swam.cleanlabel.dtos.ProductCategoryDTO;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.service.ProductCategoryService;
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
 *   POST / PUT       — CORPORATE only
 *   DELETE           — CORPORATE only
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService categoryService;
    private final RoleGuard roleGuard;

    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductCategoryDTO> create(
            @Valid @RequestBody ProductCategoryDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        ProductCategoryDTO created = categoryService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductCategoryDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}