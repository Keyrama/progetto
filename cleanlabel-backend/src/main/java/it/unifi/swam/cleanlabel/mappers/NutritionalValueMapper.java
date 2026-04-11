package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.NutritionalValueDTO;
import it.unifi.swam.cleanlabel.model.NutritionalValue;

public class NutritionalValueMapper {

    public static NutritionalValueDTO toDTO(NutritionalValue nv) {
        if (nv == null) return null;

        NutritionalValueDTO dto = new NutritionalValueDTO();
        dto.setId(nv.getId());
        dto.setCalories(nv.getCalories());
        dto.setProteins(nv.getProteins());
        dto.setCarbohydrates(nv.getCarbohydrates());
        dto.setSugars(nv.getSugars());
        dto.setFats(nv.getFats());
        dto.setSaturatedFats(nv.getSaturatedFats());
        dto.setSalt(nv.getSalt());
        dto.setFiber(nv.getFiber());
        return dto;
    }

    public static NutritionalValue toEntity(NutritionalValueDTO dto) {
        if (dto == null) return null;

        NutritionalValue nv = new NutritionalValue();
        nv.setCalories(dto.getCalories());
        nv.setProteins(dto.getProteins());
        nv.setCarbohydrates(dto.getCarbohydrates());
        nv.setSugars(dto.getSugars());
        nv.setFats(dto.getFats());
        nv.setSaturatedFats(dto.getSaturatedFats());
        nv.setSalt(dto.getSalt());
        nv.setFiber(dto.getFiber());
        return nv;
    }
}
