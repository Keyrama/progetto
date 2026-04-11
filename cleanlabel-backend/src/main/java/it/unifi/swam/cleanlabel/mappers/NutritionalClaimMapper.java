package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.NutritionalClaimDTO;
import it.unifi.swam.cleanlabel.model.NutritionalClaim;

public class NutritionalClaimMapper {

    public static NutritionalClaimDTO toDTO(NutritionalClaim claim) {
        if (claim == null) return null;

        NutritionalClaimDTO dto = new NutritionalClaimDTO();
        dto.setId(claim.getId());
        dto.setLabel(claim.getLabel());
        dto.setValidated(claim.isValidated());
        dto.setMisleading(claim.isMisleading());
        dto.setExplanation(claim.getExplanation());
        return dto;
    }

    public static NutritionalClaim toEntity(NutritionalClaimDTO dto) {
        if (dto == null) return null;

        NutritionalClaim claim = new NutritionalClaim();
        claim.setLabel(dto.getLabel());
        claim.setValidated(dto.isValidated());
        claim.setMisleading(dto.isMisleading());
        claim.setExplanation(dto.getExplanation());
        return claim;
    }
}
