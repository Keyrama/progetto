package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.NutritionalValueDTO;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NutritionalValueMapper {

    NutritionalValueDTO toDTO(NutritionalValue nv);

    NutritionalValue toEntity(NutritionalValueDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(NutritionalValueDTO dto, @MappingTarget NutritionalValue nv);
}