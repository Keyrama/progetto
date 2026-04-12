package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.AlternativeSuggestionDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Computes healthier product alternatives at runtime — no persistence.
 *
 * A product is considered an alternative when:
 *   - it belongs to the same category as the source product
 *   - it has a strictly higher health score
 *
 * Results are ordered by health score descending and capped by the limit param.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlternativeSuggestionService {

    private final ProductRepository productRepository;

    public List<AlternativeSuggestionDTO> findAlternatives(Long sourceProductId, int limit) {
        Product source = productRepository.findById(sourceProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", sourceProductId));

        if (source.getCategory() == null || source.getHealthScore() == null) {
            return List.of();
        }

        return productRepository
                .findByCategoryAndHealthScoreGreaterThanOrderByHealthScoreDesc(
                        source.getCategory(), source.getHealthScore())
                .stream()
                .filter(p -> !p.getId().equals(sourceProductId))
                .limit(limit)
                .map(target -> toDTO(source, target))
                .toList();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private AlternativeSuggestionDTO toDTO(Product source, Product target) {
        int delta = target.getHealthScore() - source.getHealthScore();
        return AlternativeSuggestionDTO.builder()
                .productId(target.getId())
                .productName(target.getName())
                .brand(target.getBrand())
                .healthScore(target.getHealthScore())
                .cleanLabel(target.isCleanLabel())
                .scoreDelta(delta)
                .reason(buildReason(source, target, delta))
                .build();
    }

    private String buildReason(Product source, Product target, int delta) {
        if (target.isCleanLabel() && !source.isCleanLabel()) {
            return "Clean label product with transparent, natural ingredients.";
        }
        if (delta >= 30) {
            return "Significantly healthier option in the same category (+%d points).".formatted(delta);
        }
        if (delta >= 15) {
            return "Noticeably better nutritional profile in the same category (+%d points).".formatted(delta);
        }
        return "Higher health score in the same category (+%d points).".formatted(delta);
    }
}