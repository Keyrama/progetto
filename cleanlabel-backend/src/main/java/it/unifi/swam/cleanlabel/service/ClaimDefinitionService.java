package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ClaimDefinitionMapper;
import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.repository.ClaimDefinitionRepository;
import it.unifi.swam.cleanlabel.repository.spec.ClaimDefinitionSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClaimDefinitionService {

    private final ClaimDefinitionRepository claimDefinitionRepository;
    private final ClaimDefinitionMapper claimDefinitionMapper;

    /**
     * Returns claim definitions filtered by any combination of misleading and type.
     * Filters compose correctly via JPA Specifications.
     */
    public List<ClaimDefinitionDTO> findAll(Boolean misleading, String type) {
        Specification<ClaimDefinition> spec = Specification.where(null);

        if (Boolean.TRUE.equals(misleading)) {
            spec = spec.and(ClaimDefinitionSpecifications.isMisleading());
        }

        if (type != null) {
            spec = spec.and(ClaimDefinitionSpecifications.hasClaimType(parseClaimType(type)));
        }

        return claimDefinitionMapper.toDTOList(claimDefinitionRepository.findAll(spec));
    }

    public ClaimDefinitionDTO findById(Long id) {
        return claimDefinitionMapper.toDTO(getClaimDefinitionOrThrow(id));
    }

    @Transactional
    public ClaimDefinitionDTO create(ClaimDefinitionDTO dto) {
        ClaimDefinition definition = claimDefinitionMapper.toEntity(dto);
        return claimDefinitionMapper.toDTO(claimDefinitionRepository.save(definition));
    }

    @Transactional
    public ClaimDefinitionDTO update(Long id, ClaimDefinitionDTO dto) {
        ClaimDefinition definition = getClaimDefinitionOrThrow(id);
        claimDefinitionMapper.updateEntity(dto, definition);
        return claimDefinitionMapper.toDTO(claimDefinitionRepository.save(definition));
    }

    @Transactional
    public void delete(Long id) {
        getClaimDefinitionOrThrow(id);
        claimDefinitionRepository.deleteById(id);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private ClaimDefinition getClaimDefinitionOrThrow(Long id) {
        return claimDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimDefinition", id));
    }

    private ClaimDefinition.ClaimType parseClaimType(String type) {
        try {
            return ClaimDefinition.ClaimType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid claim type: '" + type + "'. Allowed: NUTRITIONAL, HEALTH, MARKETING");
        }
    }
}