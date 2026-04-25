package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ClaimDefinitionDTO;
import it.unifi.swam.cleanlabel.model.ClaimDefinition;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-25T18:35:23+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class ClaimDefinitionMapperImpl implements ClaimDefinitionMapper {

    @Override
    public ClaimDefinitionDTO toDTO(ClaimDefinition claimDefinition) {
        if ( claimDefinition == null ) {
            return null;
        }

        ClaimDefinitionDTO.ClaimDefinitionDTOBuilder claimDefinitionDTO = ClaimDefinitionDTO.builder();

        claimDefinitionDTO.id( claimDefinition.getId() );
        claimDefinitionDTO.term( claimDefinition.getTerm() );
        claimDefinitionDTO.claimType( claimDefinition.getClaimType() );
        claimDefinitionDTO.regulated( claimDefinition.isRegulated() );
        claimDefinitionDTO.misleading( claimDefinition.isMisleading() );
        claimDefinitionDTO.misleadingReason( claimDefinition.getMisleadingReason() );
        claimDefinitionDTO.explanation( claimDefinition.getExplanation() );
        claimDefinitionDTO.regulatoryReference( claimDefinition.getRegulatoryReference() );
        claimDefinitionDTO.validationStrategy( claimDefinition.getValidationStrategy() );
        claimDefinitionDTO.validationThreshold( claimDefinition.getValidationThreshold() );

        return claimDefinitionDTO.build();
    }

    @Override
    public List<ClaimDefinitionDTO> toDTOList(List<ClaimDefinition> claimDefinitions) {
        if ( claimDefinitions == null ) {
            return null;
        }

        List<ClaimDefinitionDTO> list = new ArrayList<ClaimDefinitionDTO>( claimDefinitions.size() );
        for ( ClaimDefinition claimDefinition : claimDefinitions ) {
            list.add( toDTO( claimDefinition ) );
        }

        return list;
    }

    @Override
    public ClaimDefinition toEntity(ClaimDefinitionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ClaimDefinition claimDefinition = new ClaimDefinition();

        claimDefinition.setId( dto.getId() );
        claimDefinition.setTerm( dto.getTerm() );
        claimDefinition.setClaimType( dto.getClaimType() );
        claimDefinition.setRegulated( dto.isRegulated() );
        claimDefinition.setMisleading( dto.isMisleading() );
        claimDefinition.setMisleadingReason( dto.getMisleadingReason() );
        claimDefinition.setExplanation( dto.getExplanation() );
        claimDefinition.setRegulatoryReference( dto.getRegulatoryReference() );
        claimDefinition.setValidationStrategy( dto.getValidationStrategy() );
        claimDefinition.setValidationThreshold( dto.getValidationThreshold() );

        return claimDefinition;
    }

    @Override
    public void updateEntity(ClaimDefinitionDTO dto, ClaimDefinition claimDefinition) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            claimDefinition.setId( dto.getId() );
        }
        if ( dto.getTerm() != null ) {
            claimDefinition.setTerm( dto.getTerm() );
        }
        if ( dto.getClaimType() != null ) {
            claimDefinition.setClaimType( dto.getClaimType() );
        }
        claimDefinition.setRegulated( dto.isRegulated() );
        claimDefinition.setMisleading( dto.isMisleading() );
        if ( dto.getMisleadingReason() != null ) {
            claimDefinition.setMisleadingReason( dto.getMisleadingReason() );
        }
        if ( dto.getExplanation() != null ) {
            claimDefinition.setExplanation( dto.getExplanation() );
        }
        if ( dto.getRegulatoryReference() != null ) {
            claimDefinition.setRegulatoryReference( dto.getRegulatoryReference() );
        }
        if ( dto.getValidationStrategy() != null ) {
            claimDefinition.setValidationStrategy( dto.getValidationStrategy() );
        }
        if ( dto.getValidationThreshold() != null ) {
            claimDefinition.setValidationThreshold( dto.getValidationThreshold() );
        }
    }
}
