package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.model.Allergen;

public class AllergenMapper {

    public static AllergenDTO toDTO(Allergen allergen) {
        if (allergen == null) return null;

        AllergenDTO dto = new AllergenDTO();
        dto.setId(allergen.getId());
        dto.setName(allergen.getName());
        dto.setCode(allergen.getCode());
        dto.setDescription(allergen.getDescription());
        return dto;
    }

    public static Allergen toEntity(AllergenDTO dto) {
        if (dto == null) return null;

        Allergen allergen = new Allergen();
        allergen.setName(dto.getName());
        allergen.setCode(dto.getCode());
        allergen.setDescription(dto.getDescription());
        return allergen;
    }
}
