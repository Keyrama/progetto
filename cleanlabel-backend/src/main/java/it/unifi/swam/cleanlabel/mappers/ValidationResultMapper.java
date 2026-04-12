package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ValidationResultDTO;
import it.unifi.swam.cleanlabel.model.ValidationResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValidationResultMapper {
    ValidationResultDTO toDTO(ValidationResult validationResult);
    ValidationResult toEntity(ValidationResultDTO dto);
}