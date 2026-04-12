package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ProductMapper;
import it.unifi.swam.cleanlabel.model.*;
import it.unifi.swam.cleanlabel.repository.AllergenRepository;
import it.unifi.swam.cleanlabel.repository.IngredientRepository;
import it.unifi.swam.cleanlabel.repository.ProductCategoryRepository;
import it.unifi.swam.cleanlabel.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
     * Returns products filtered by optional search, category, and cleanLabel params.
     * Uses summary DTO (no claims/ingredients detail) to avoid N+1 queries on lists.
     */
    public List<ProductDTO> findAll(String search, Long categoryId, Boolean cleanLabel) {
        List<Product> products;

        if (search != null && !search.isBlank()) {
            products = productRepository.searchByNameOrBrand(search.trim());
        } else if (categoryId != null) {
            ProductCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", categoryId));
            products = productRepository.findByCategory(category);
        } else if (Boolean.TRUE.equals(cleanLabel)) {
            products = productRepository.findByCleanLabelTrue();
        } else {
            products = productRepository.findAll();
        }

        return productMapper.toSummaryDTOList(products);
    }

    /**
     * Returns the full product detail including ingredients, allergens,
     * nutritional values, and persisted claim analysis results.
     */
    public ProductDTO findById(Long id) {
        return productMapper.toDTO(getProductOrThrow(id));
    }

    // ── Mutations ─────────────────────────────────────────────────────────────

    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product product = new Product();
        productMapper.updateEntityFromDTO(dto, product);

        resolveAndSetRelationships(product, dto);

        // Health score and cleanLabel are always computed — never taken from input
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

    /**
     * Resolves all relationship fields from IDs in the DTO to managed entities.
     * Done in the service because each lookup requires a database call.
     */
    private void resolveAndSetRelationships(Product product, ProductDTO dto) {
        // Category
        if (dto.getCategoryId() != null) {
            ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "ProductCategory", dto.getCategoryId()));
            product.setCategory(category);
        }

        // Ingredients — replace entire list on each update
        if (dto.getIngredientIds() != null) {
            List<Ingredient> ingredients =
                    ingredientRepository.findAllById(dto.getIngredientIds());
            if (ingredients.size() != dto.getIngredientIds().size()) {
                throw new ResourceNotFoundException(
                        "One or more ingredient IDs not found");
            }
            product.setIngredients(new ArrayList<>(ingredients));
        }

        // "May contain allergens" (cross-contamination traces)
        if (dto.getMayContainAllergenIds() != null) {
            List<Allergen> allergens =
                    allergenRepository.findAllById(dto.getMayContainAllergenIds());
            if (allergens.size() != dto.getMayContainAllergenIds().size()) {
                throw new ResourceNotFoundException(
                        "One or more allergen IDs not found");
            }
            product.setMayContainAllergens(new ArrayList<>(allergens));
        }
    }

    public Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
}