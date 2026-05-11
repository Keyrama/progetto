package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaimDefinitionRepository extends JpaRepository<ClaimDefinition, Long>,
        JpaSpecificationExecutor<ClaimDefinition> {

    Optional<ClaimDefinition> findByTermIgnoreCase(String term);
}