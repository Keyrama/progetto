package it.unifi.swam.cleanlabel.repository;

import it.unifi.swam.cleanlabel.model.ProductClaim;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductClaimRepository extends JpaRepository<ProductClaim, Long> {

    List<ProductClaim> findByProductId(Long productId);

    @Query("SELECT pc FROM ProductClaim pc " +
            "WHERE pc.product.id = :productId " +
            "AND pc.claimDefinition.misleading = true")
    List<ProductClaim> findMisleadingByProductId(@Param("productId") Long productId);

    @Query("SELECT pc FROM ProductClaim pc " +
            "WHERE pc.product.id = :productId " +
            "AND pc.validationResult.verdict = :verdict")
    List<ProductClaim> findByProductIdAndVerdict(
            @Param("productId") Long productId,
            @Param("verdict") ValidationResult.Verdict verdict);
}