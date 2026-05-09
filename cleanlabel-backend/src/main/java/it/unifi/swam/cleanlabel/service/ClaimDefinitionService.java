package it.unifi.swam.cleanlabel.service;

import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.exception.ResourceNotFoundException;
import it.unifi.swam.cleanlabel.mappers.ClaimDefinitionMapper;
import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import it.unifi.swam.cleanlabel.repository.ClaimDefinitionRepository;
import it.unifi.swam.cleanlabel.repository.spec.ClaimDefinitionSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    public List<ClaimDefinitionDTO> findAll(Boolean misleading, String type, String search,
                                            Integer limit, Integer offset) {
        Specification<ClaimDefinition> spec = buildSpec(misleading, type, search);

        if (limit != null && limit > 0) {
            int page = (offset != null ? offset : 0) / limit;
            return claimDefinitionMapper.toDTOList(
                    claimDefinitionRepository.findAll(spec, PageRequest.of(page, limit)).getContent());
        }

        return claimDefinitionMapper.toDTOList(claimDefinitionRepository.findAll(spec));
    }

    public long count(Boolean misleading, String type, String search) {
        return claimDefinitionRepository.count(buildSpec(misleading, type, search));
    }

    private Specification<ClaimDefinition> buildSpec(Boolean misleading, String type, String search) {
        Specification<ClaimDefinition> spec = Specification.where(null);
        if (Boolean.TRUE.equals(misleading))
            spec = spec.and(ClaimDefinitionSpecifications.isMisleading());
        if (type != null)
            spec = spec.and(ClaimDefinitionSpecifications.hasClaimType(parseClaimType(type)));
        if (search != null && !search.isBlank())
            spec = spec.and(ClaimDefinitionSpecifications.termContains(search.trim()));
        return spec;
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