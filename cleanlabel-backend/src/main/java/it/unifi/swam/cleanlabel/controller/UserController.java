package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.config.RoleGuard;
import it.unifi.swam.cleanlabel.dtos.UserDTO;
import it.unifi.swam.cleanlabel.model.User;
import it.unifi.swam.cleanlabel.service.UserService;
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
 *   GET /api/users/mock/{role}  — open (used to simulate login)
 *   GET /api/users              — CORPORATE only
 *   GET /api/users/{id}         — CORPORATE only
 *   POST / PUT / DELETE         — CORPORATE only
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleGuard roleGuard;

    /**
     * Mock login — always open, no role required.
     * Must be declared BEFORE /{id} to avoid Spring matching "mock" as an id.
     */
    @GetMapping("/mock/{role}")
    public ResponseEntity<UserDTO> getMockUser(@PathVariable String role) {
        return ResponseEntity.ok(userService.getMockUser(role));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll(HttpServletRequest request) {
        roleGuard.require(request, User.Role.CORPORATE);
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(
            @Valid @RequestBody UserDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        UserDTO created = userService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO dto,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {

        roleGuard.require(request, User.Role.CORPORATE);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}