package it.unifi.swam.cleanlabel.repository.spec;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import org.springframework.data.jpa.domain.Specification;

public class ClaimDefinitionSpecifications {

    private ClaimDefinitionSpecifications() {}

    public static Specification<ClaimDefinition> isMisleading() {
        return (root, cq, cb) -> cb.isTrue(root.get("misleading"));
    }

    public static Specification<ClaimDefinition> hasClaimType(ClaimDefinition.ClaimType type) {
        return (root, cq, cb) -> cb.equal(root.get("claimType"), type);
    }
}