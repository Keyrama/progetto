package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ClaimDefinitionRepository extends JpaRepository<ClaimDefinition, Long> {
    Optional<ClaimDefinition> findByTermIgnoreCase(String term);
    List<ClaimDefinition> findByMisleadingTrue();
    List<ClaimDefinition> findByClaimType(ClaimDefinition.ClaimType claimType);
}