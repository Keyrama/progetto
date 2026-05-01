package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ProductMapper;
import it.unifi.swam.cleanlabel.model.*;
import it.unifi.swam.cleanlabel.repository.AllergenRepository;
import it.unifi.swam.cleanlabel.repository.IngredientRepository;
import it.unifi.swam.cleanlabel.repository.ProductCategoryRepository;
import it.unifi.swam.cleanlabel.repository.ProductRepository;
import it.unifi.swam.cleanlabel.repository.spec.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final AllergenRepository allergenRepository;
    private final ProductMapper productMapper;
    private final HealthScoreService healthScoreService;

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * Returns products filtered by any combination of search, category, and
     * cleanLabel. All active filters are applied simultaneously via JPA
     * Specifications — unlike a simple if-else chain, multiple filters
     * compose correctly (e.g. search + cleanLabel works as expected).
     */
    public List<ProductDTO> findAll(String search, Long categoryId, Boolean cleanLabel) {
        Specification<Product> spec = Specification.where(null);

        if (search != null && !search.isBlank()) {
            spec = spec.and(ProductSpecifications.nameOrBrandContains(search.trim()));
        }

        if (categoryId != null) {
            ProductCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", categoryId));
            spec = spec.and(ProductSpecifications.inCategory(category));
        }

        if (Boolean.TRUE.equals(cleanLabel)) {
            spec = spec.and(ProductSpecifications.cleanLabelOnly());
        }

        return productMapper.toSummaryDTOList(productRepository.findAll(spec));
    }

    public ProductDTO findById(Long id) {
        return productMapper.toDTO(getProductOrThrow(id));
    }

    // ── Mutations ─────────────────────────────────────────────────────────────

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product();
        productMapper.updateEntityFromDTO(dto, product);
        resolveAndSetRelationships(product, dto);
        healthScoreService.computeAndApply(product);
        return productMapper.toDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = getProductOrThrow(id);
        productMapper.updateEntityFromDTO(dto, product);
        resolveAndSetRelationships(product, dto);
        healthScoreService.computeAndApply(product);
        return productMapper.toDTO(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        getProductOrThrow(id);
        productRepository.deleteById(id);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private void resolveAndSetRelationships(Product product, ProductDTO dto) {
        if (dto.getCategoryId() != null) {
            ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "ProductCategory", dto.getCategoryId()));
            product.setCategory(category);
        }

        if (dto.getIngredientIds() != null) {
            List<Ingredient> ingredients =
                    ingredientRepository.findAllById(dto.getIngredientIds());
            if (ingredients.size() != dto.getIngredientIds().size()) {
                throw new ResourceNotFoundException("One or more ingredient IDs not found");
            }
            product.setIngredients(new ArrayList<>(ingredients));
        }

        if (dto.getMayContainAllergenIds() != null) {
            List<Allergen> allergens =
                    allergenRepository.findAllById(dto.getMayContainAllergenIds());
            if (allergens.size() != dto.getMayContainAllergenIds().size()) {
                throw new ResourceNotFoundException("One or more allergen IDs not found");
            }
            product.setMayContainAllergens(new ArrayList<>(allergens));
        }
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
}