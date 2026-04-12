package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ClaimDefinitionMapper;
import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.repository.ClaimDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClaimDefinitionService {

    private final ClaimDefinitionRepository claimDefinitionRepository;
    private final ClaimDefinitionMapper claimDefinitionMapper;

    public List<ClaimDefinitionDTO> findAll(Boolean misleading, String type) {
        List<ClaimDefinition> results;

        if (Boolean.TRUE.equals(misleading)) {
            results = claimDefinitionRepository.findByMisleadingTrue();
        } else if (type != null) {
            ClaimDefinition.ClaimType claimType = parseClaimType(type);
            results = claimDefinitionRepository.findByClaimType(claimType);
        } else {
            results = claimDefinitionRepository.findAll();
        }

        return claimDefinitionMapper.toDTOList(results);
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

    public ClaimDefinition getClaimDefinitionOrThrow(Long id) {
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