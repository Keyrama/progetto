package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.model.Allergen;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AllergenMapper {

    AllergenDTO toDTO(Allergen allergen);

    List<AllergenDTO> toDTOList(List<Allergen> allergens);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Allergen toEntity(AllergenDTO dto);
}