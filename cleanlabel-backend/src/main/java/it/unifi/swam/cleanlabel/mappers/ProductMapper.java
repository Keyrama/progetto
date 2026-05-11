package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                NutritionalValueMapper.class,
                IngredientMapper.class,
                AllergenMapper.class
        })
public interface ProductMapper {

    @Mapping(target = "categoryId",   source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "declaredAllergens", expression = "java(allergenMapper.toDTOList(product.getDeclaredAllergens()))")
    @Mapping(target = "ingredientIds", expression = "java(product.getIngredients().stream().map(i -> i.getId()).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "mayContainAllergenIds", expression = "java(product.getMayContainAllergens().stream().map(a -> a.getId()).collect(java.util.stream.Collectors.toList()))")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOList(List<Product> products);

    @Named("toSummaryDTO")
    @Mapping(target = "categoryId",            source = "category.id")
    @Mapping(target = "categoryName",          source = "category.name")
    @Mapping(target = "declaredAllergens",     ignore = true)
    @Mapping(target = "ingredients",           ignore = true)
    @Mapping(target = "ingredientIds",         ignore = true)
    @Mapping(target = "mayContainAllergens",   ignore = true)
    @Mapping(target = "mayContainAllergenIds", ignore = true)
    @Mapping(target = "nutritionalValue",      ignore = true)
    ProductDTO toSummaryDTO(Product product);

    @IterableMapping(qualifiedByName = "toSummaryDTO")
    List<ProductDTO> toSummaryDTOList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category",            ignore = true)
    @Mapping(target = "ingredients",         ignore = true)
    @Mapping(target = "mayContainAllergens", ignore = true)
    @Mapping(target = "healthScore",         ignore = true)
    @Mapping(target = "cleanLabel",          ignore = true)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product product);
}