package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByBrandIgnoreCase(String brand);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByCleanLabelTrue();

    /**
     * Used by AlternativeSuggestionService:
     * finds products in the same category with a higher health score,
     * ordered best-first.
     */
    List<Product> findByCategoryAndHealthScoreGreaterThanOrderByHealthScoreDesc(
            ProductCategory category, Integer healthScore);

    /**
     * Full-text search on name and brand (case-insensitive).
     */
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchByNameOrBrand(@Param("query") String query);
}