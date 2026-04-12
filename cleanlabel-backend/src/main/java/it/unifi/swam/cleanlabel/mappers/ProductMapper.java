package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.model.Product;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
                NutritionalValueMapper.class,
                IngredientMapper.class,
                AllergenMapper.class,
                ProductClaimMapper.class
        })
public interface ProductMapper {

    /**
     * Full mapping for GET responses.
     * - categoryId and categoryName are sourced from product.category
     * - declaredAllergens comes from the @Transient helper on Product
     * - ingredientIds are extracted from the ingredients list
     * - mayContainAllergenIds are extracted from mayContainAllergens list
     */
    @Mapping(target = "categoryId",   source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "declaredAllergens", expression = "java(allergenMapper.toDTOList(product.getDeclaredAllergens()))")
    @Mapping(target = "ingredientIds", expression = "java(product.getIngredients().stream().map(i -> i.getId()).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "mayContainAllergenIds", expression = "java(product.getMayContainAllergens().stream().map(a -> a.getId()).collect(java.util.stream.Collectors.toList()))")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOList(List<Product> products);

    /**
     * Minimal mapping for list views (no claims, no ingredients detail).
     * Useful for search results and alternatives — avoids N+1 queries.
     */
    @Named("toSummaryDTO")
    @Mapping(target = "categoryId",          source = "category.id")
    @Mapping(target = "categoryName",        source = "category.name")
    @Mapping(target = "declaredAllergens",   ignore = true)
    @Mapping(target = "ingredients",         ignore = true)
    @Mapping(target = "ingredientIds",       ignore = true)
    @Mapping(target = "mayContainAllergens", ignore = true)
    @Mapping(target = "mayContainAllergenIds", ignore = true)
    @Mapping(target = "claims",              ignore = true)
    @Mapping(target = "nutritionalValue",    ignore = true)
    ProductDTO toSummaryDTO(Product product);

    @IterableMapping(qualifiedByName = "toSummaryDTO")
    List<ProductDTO> toSummaryDTOList(List<Product> products);

    /**
     * Used by ProductService on create/update.
     * Relationships (category, ingredients, mayContainAllergens) are resolved
     * by the service from IDs — ignored here to avoid incomplete mappings.
     * Computed fields (healthScore, cleanLabel) are also ignored.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category",              ignore = true)
    @Mapping(target = "ingredients",           ignore = true)
    @Mapping(target = "mayContainAllergens",   ignore = true)
    @Mapping(target = "claims",                ignore = true)
    @Mapping(target = "healthScore",           ignore = true)
    @Mapping(target = "cleanLabel",            ignore = true)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product product);
}