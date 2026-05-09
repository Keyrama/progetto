package it.unifi.swam.cleanlabel.repository.spec;

import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import org.springframework.data.jpa.domain.Specification;

public class ClaimDefinitionSpecifications {

    private ClaimDefinitionSpecifications() {}

    public static Specification<ClaimDefinition> hasMisleading(boolean misleading) {
        return (root, cq, cb) -> misleading
                ? cb.isTrue(root.get("misleading"))
                : cb.isFalse(root.get("misleading"));
    }

    public static Specification<ClaimDefinition> hasClaimType(ClaimDefinition.ClaimType type) {
        return (root, cq, cb) -> cb.equal(root.get("claimType"), type);
    }

    public static Specification<ClaimDefinition> termContains(String query) {
        return (root, cq, cb) ->
                cb.like(cb.lower(root.get("term")), "%" + query.toLowerCase() + "%");
    }
}