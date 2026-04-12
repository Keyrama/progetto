package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ProductCategoryDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ProductCategoryMapper;
import it.unifi.swam.cleanlabel.model.ProductCategory;
import it.unifi.swam.cleanlabel.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;
    private final ProductCategoryMapper categoryMapper;

    public List<ProductCategoryDTO> findAll() {
        return categoryMapper.toDTOList(categoryRepository.findAll());
    }

    public ProductCategoryDTO findById(Long id) {
        return categoryMapper.toDTO(getCategoryOrThrow(id));
    }

    @Transactional
    public ProductCategoryDTO create(ProductCategoryDTO dto) {
        ProductCategory category = categoryMapper.toEntity(dto);
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Transactional
    public ProductCategoryDTO update(Long id, ProductCategoryDTO dto) {
        ProductCategory category = getCategoryOrThrow(id);
        categoryMapper.updateEntity(dto, category);
        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        getCategoryOrThrow(id);
        categoryRepository.deleteById(id);
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    public ProductCategory getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", id));
    }
}