package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClaimDefinitionMapper {

    ClaimDefinitionDTO toDTO(ClaimDefinition claimDefinition);

    List<ClaimDefinitionDTO> toDTOList(List<ClaimDefinition> claimDefinitions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClaimDefinition toEntity(ClaimDefinitionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ClaimDefinitionDTO dto, @MappingTarget ClaimDefinition claimDefinition);
}