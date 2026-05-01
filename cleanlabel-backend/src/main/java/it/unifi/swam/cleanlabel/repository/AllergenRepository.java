package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
}