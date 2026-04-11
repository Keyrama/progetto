package it.unifi.swam.cleanlabel.model;

import jakarta.persistence.*;

/**
 * Represents a registered user of the Clean Label platform.
 * Authentication is mocked; this class only stores role and identity data.
 */
@Entity
@Table(name = "users")
public class User {

    public enum Role { CONSUMER, SPECIALIST, CORPORATE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ── Constructors ──────────────────────────────────────────────────────────

    public User() {}

    public User(String username, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
