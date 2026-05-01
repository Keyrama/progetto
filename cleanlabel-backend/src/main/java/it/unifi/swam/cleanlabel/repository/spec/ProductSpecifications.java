package it.unifi.swam.cleanlabel.repository.spec;

import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ProductCategory;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA Specification predicates for Product filtering.
 *
 * Each method returns an independent, composable Specification.
 * They can be combined with Specification.where().and().and()...
 * allowing any combination of filters in a single query —
 * unlike the previous if-else chain which only applied one filter at a time.
 *
 * Usage example:
 *   Specification<Product> spec = Specification
 *       .where(ProductSpecifications.namOrBrandContains("bio"))
 *       .and(ProductSpecifications.cleanLabelOnly());
 *   productRepository.findAll(spec);
 */
public class ProductSpecifications {

    private ProductSpecifications() {}

    public static Specification<Product> nameOrBrandContains(String query) {
        return (root, cq, cb) -> {
            String pattern = "%" + query.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")),  pattern),
                    cb.like(cb.lower(root.get("brand")), pattern)
            );
        };
    }

    public static Specification<Product> inCategory(ProductCategory category) {
        return (root, cq, cb) -> cb.equal(root.get("category"), category);
    }

    public static Specification<Product> cleanLabelOnly() {
        return (root, cq, cb) -> cb.isTrue(root.get("cleanLabel"));
    }
}