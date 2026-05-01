package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ProductClaimDTO;
import it.unifi.swam.cleanlabel.model.ProductClaim;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class ProductClaimMapperImpl implements ProductClaimMapper {

    @Autowired
    private ClaimDefinitionMapper claimDefinitionMapper;
    @Autowired
    private ValidationResultMapper validationResultMapper;

    @Override
    public ProductClaimDTO toDTO(ProductClaim productClaim) {
        if ( productClaim == null ) {
            return null;
        }

        ProductClaimDTO.ProductClaimDTOBuilder productClaimDTO = ProductClaimDTO.builder();

        productClaimDTO.claimDefinition( claimDefinitionMapper.toDTO( productClaim.getClaimDefinition() ) );
        productClaimDTO.validationResult( validationResultMapper.toDTO( productClaim.getValidationResult() ) );
        productClaimDTO.id( productClaim.getId() );
        productClaimDTO.rawLabel( productClaim.getRawLabel() );
        productClaimDTO.status( productClaim.getStatus() );
        productClaimDTO.analyzedAt( productClaim.getAnalyzedAt() );

        return productClaimDTO.build();
    }

    @Override
    public List<ProductClaimDTO> toDTOList(List<ProductClaim> productClaims) {
        if ( productClaims == null ) {
            return null;
        }

        List<ProductClaimDTO> list = new ArrayList<ProductClaimDTO>( productClaims.size() );
        for ( ProductClaim productClaim : productClaims ) {
            list.add( toDTO( productClaim ) );
        }

        return list;
    }
}
