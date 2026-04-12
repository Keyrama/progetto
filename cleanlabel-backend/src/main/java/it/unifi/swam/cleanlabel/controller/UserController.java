package it.unifi.swam.cleanlabel.controller;

import it.unifi.swam.cleanlabel.dtos.UserDTO;
import it.unifi.swam.cleanlabel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for User resources.
 *
 * Base path: /api/users
 *
 * NOTE: Authentication is mocked per assignment requirements.
 * GET /api/users/mock/{role} returns a pre-built mock user of the given role,
 * simulating a logged-in session without implementing actual auth.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO dto) {
        UserDTO created = userService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Mock auth ──────────────────────────────────────────────────────────────

    /**
     * Returns a mock logged-in user for the given role.
     * Used by the FE to simulate authentication without implementing real auth.
     * E.g. GET /api/users/mock/CONSUMER
     */
    @GetMapping("/mock/{role}")
    public ResponseEntity<UserDTO> getMockUser(@PathVariable String role) {
        return ResponseEntity.ok(userService.getMockUser(role));
    }
}