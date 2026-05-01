package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ValidationResultDTO;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class ValidationResultMapperImpl implements ValidationResultMapper {

    @Override
    public ValidationResultDTO toDTO(ValidationResult validationResult) {
        if ( validationResult == null ) {
            return null;
        }

        ValidationResultDTO.ValidationResultDTOBuilder validationResultDTO = ValidationResultDTO.builder();

        validationResultDTO.verdict( validationResult.getVerdict() );
        validationResultDTO.validationDetail( validationResult.getValidationDetail() );

        return validationResultDTO.build();
    }
}
