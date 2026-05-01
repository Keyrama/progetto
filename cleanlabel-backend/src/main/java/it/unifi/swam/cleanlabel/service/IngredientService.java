package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.IngredientMapper;
import it.unifi.swam.cleanlabel.model.Allergen;
import it.unifi.swam.cleanlabel.model.Ingredient;
import it.unifi.swam.cleanlabel.repository.AllergenRepository;
import it.unifi.swam.cleanlabel.repository.IngredientRepository;
import it.unifi.swam.cleanlabel.repository.spec.IngredientSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final AllergenRepository allergenRepository;
    private final IngredientMapper ingredientMapper;

    /**
     * Returns ingredients filtered by any combination of artificial and riskLevel.
     * Filters compose correctly via JPA Specifications.
     */
    public List<IngredientDTO> findAll(Boolean artificial, String riskLevel) {
        Specification<Ingredient> spec = Specification.where(null);

        if (Boolean.TRUE.equals(artificial)) {
            spec = spec.and(IngredientSpecifications.isArtificial());
        }

        if (riskLevel != null) {
            spec = spec.and(IngredientSpecifications.hasRiskLevel(parseRiskLevel(riskLevel)));
        }

        return ingredientMapper.toDTOList(ingredientRepository.findAll(spec));
    }

    public IngredientDTO findById(Long id) {
        return ingredientMapper.toDTO(getIngredientOrThrow(id));
    }

    @Transactional
    public IngredientDTO create(IngredientDTO dto) {
        Ingredient ingredient = ingredientMapper.toEntity(dto);
        resolveAndSetAllergens(ingredient, dto);
        return ingredientMapper.toDTO(ingredientRepository.save(ingredient));
    }

    @Transactional
    public IngredientDTO update(Long id, IngredientDTO dto) {
        Ingredient ingredient = getIngredientOrThrow(id);
        ingredientMapper.updateEntity(dto, ingredient);
        resolveAndSetAllergens(ingredient, dto);
        return ingredientMapper.toDTO(ingredientRepository.save(ingredient));
    }

    @Transactional
    public void delete(Long id) {
        getIngredientOrThrow(id);
        ingredientRepository.deleteById(id);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private void resolveAndSetAllergens(Ingredient ingredient, IngredientDTO dto) {
        if (dto.getAllergens() == null || dto.getAllergens().isEmpty()) {
            ingredient.setAllergens(new ArrayList<>());
            return;
        }

        List<Long> allergenIds = dto.getAllergens().stream()
                .map(a -> a.getId())
                .toList();

        List<Allergen> allergens = allergenRepository.findAllById(allergenIds);

        if (allergens.size() != allergenIds.size()) {
            throw new ResourceNotFoundException("One or more allergen IDs not found");
        }

        ingredient.setAllergens(allergens);
    }

    private Ingredient.RiskLevel parseRiskLevel(String riskLevel) {
        try {
            return Ingredient.RiskLevel.valueOf(riskLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid riskLevel value: '" + riskLevel + "'. Allowed: LOW, MEDIUM, HIGH");
        }
    }

    private Ingredient getIngredientOrThrow(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", id));
    }
}