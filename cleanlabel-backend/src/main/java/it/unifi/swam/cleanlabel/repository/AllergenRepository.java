package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
    Optional<Allergen> findByCodeIgnoreCase(String code);
}