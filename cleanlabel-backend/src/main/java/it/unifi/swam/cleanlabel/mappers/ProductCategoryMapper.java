package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ProductCategoryDTO;
import it.unifi.swam.cleanlabel.model.ProductCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryDTO toDTO(ProductCategory category);

    List<ProductCategoryDTO> toDTOList(List<ProductCategory> categories);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductCategory toEntity(ProductCategoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductCategoryDTO dto, @MappingTarget ProductCategory category);
}