package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ProductClaimDTO;
import it.unifi.swam.cleanlabel.model.ProductClaim;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClaimDefinitionMapper.class, ValidationResultMapper.class})
public interface ProductClaimMapper {

    @Mapping(target = "claimDefinition", source = "claimDefinition")
    @Mapping(target = "validationResult", source = "validationResult")
    ProductClaimDTO toDTO(ProductClaim productClaim);

    List<ProductClaimDTO> toDTOList(List<ProductClaim> productClaims);
}