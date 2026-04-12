package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.IngredientMapper;
import it.unifi.swam.cleanlabel.model.Allergen;
import it.unifi.swam.cleanlabel.model.Ingredient;
import it.unifi.swam.cleanlabel.repository.AllergenRepository;
import it.unifi.swam.cleanlabel.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
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

    public List<IngredientDTO> findAll(Boolean artificial, String riskLevel) {
        List<Ingredient> results;

        if (Boolean.TRUE.equals(artificial)) {
            results = ingredientRepository.findByArtificialTrue();
        } else if (riskLevel != null) {
            Ingredient.RiskLevel level = parseRiskLevel(riskLevel);
            results = ingredientRepository.findByRiskLevel(level);
        } else {
            results = ingredientRepository.findAll();
        }

        return ingredientMapper.toDTOList(results);
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

    /**
     * Resolves allergen IDs from the DTO into managed Allergen entities
     * and sets them on the ingredient.
     * This is intentionally done in the service rather than the mapper
     * because it requires a database lookup.
     */
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

    public Ingredient getIngredientOrThrow(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", id));
    }
}