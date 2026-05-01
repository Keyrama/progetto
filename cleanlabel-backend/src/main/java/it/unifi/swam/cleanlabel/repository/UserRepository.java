package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Used by UserService.getMockUser() to find the pre-seeded mock user by role
    Optional<User> findByUsernameIgnoreCase(String username);}