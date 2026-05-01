package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ProductCategoryDTO;
import it.unifi.swam.cleanlabel.model.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategoryDTO toDTO(ProductCategory category) {
        if ( category == null ) {
            return null;
        }

        ProductCategoryDTO.ProductCategoryDTOBuilder productCategoryDTO = ProductCategoryDTO.builder();

        productCategoryDTO.id( category.getId() );
        productCategoryDTO.name( category.getName() );
        productCategoryDTO.description( category.getDescription() );

        return productCategoryDTO.build();
    }

    @Override
    public List<ProductCategoryDTO> toDTOList(List<ProductCategory> categories) {
        if ( categories == null ) {
            return null;
        }

        List<ProductCategoryDTO> list = new ArrayList<ProductCategoryDTO>( categories.size() );
        for ( ProductCategory productCategory : categories ) {
            list.add( toDTO( productCategory ) );
        }

        return list;
    }

    @Override
    public ProductCategory toEntity(ProductCategoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductCategory productCategory = new ProductCategory();

        productCategory.setId( dto.getId() );
        productCategory.setName( dto.getName() );
        productCategory.setDescription( dto.getDescription() );

        return productCategory;
    }

    @Override
    public void updateEntity(ProductCategoryDTO dto, ProductCategory category) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            category.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            category.setName( dto.getName() );
        }
        if ( dto.getDescription() != null ) {
            category.setDescription( dto.getDescription() );
        }
    }
}
