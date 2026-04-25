package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.dtos.ProductDTO;
import it.unifi.swam.cleanlabel.model.Allergen;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import it.unifi.swam.cleanlabel.model.Product;
import it.unifi.swam.cleanlabel.model.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-25T18:35:22+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private NutritionalValueMapper nutritionalValueMapper;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private AllergenMapper allergenMapper;
    @Autowired
    private ProductClaimMapper productClaimMapper;

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.categoryId( productCategoryId( product ) );
        productDTO.categoryName( productCategoryName( product ) );
        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.brand( product.getBrand() );
        productDTO.description( product.getDescription() );
        productDTO.nutritionalValue( nutritionalValueMapper.toDTO( product.getNutritionalValue() ) );
        productDTO.ingredients( ingredientMapper.toDTOList( product.getIngredients() ) );
        productDTO.mayContainAllergens( allergenMapper.toDTOList( product.getMayContainAllergens() ) );
        productDTO.sustainabilityScore( product.getSustainabilityScore() );
        productDTO.healthScore( product.getHealthScore() );
        productDTO.cleanLabel( product.isCleanLabel() );
        productDTO.claims( productClaimMapper.toDTOList( product.getClaims() ) );

        productDTO.declaredAllergens( allergenMapper.toDTOList(product.getDeclaredAllergens()) );
        productDTO.ingredientIds( product.getIngredients().stream().map(i -> i.getId()).collect(java.util.stream.Collectors.toList()) );
        productDTO.mayContainAllergenIds( product.getMayContainAllergens().stream().map(a -> a.getId()).collect(java.util.stream.Collectors.toList()) );

        return productDTO.build();
    }

    @Override
    public List<ProductDTO> toDTOList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( products.size() );
        for ( Product product : products ) {
            list.add( toDTO( product ) );
        }

        return list;
    }

    @Override
    public ProductDTO toSummaryDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.categoryId( productCategoryId( product ) );
        productDTO.categoryName( productCategoryName( product ) );
        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.brand( product.getBrand() );
        productDTO.description( product.getDescription() );
        productDTO.sustainabilityScore( product.getSustainabilityScore() );
        productDTO.healthScore( product.getHealthScore() );
        productDTO.cleanLabel( product.isCleanLabel() );

        return productDTO.build();
    }

    @Override
    public List<ProductDTO> toSummaryDTOList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( products.size() );
        for ( Product product : products ) {
            list.add( toSummaryDTO( product ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDTO(ProductDTO dto, Product product) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            product.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            product.setName( dto.getName() );
        }
        if ( dto.getBrand() != null ) {
            product.setBrand( dto.getBrand() );
        }
        if ( dto.getDescription() != null ) {
            product.setDescription( dto.getDescription() );
        }
        if ( dto.getSustainabilityScore() != null ) {
            product.setSustainabilityScore( dto.getSustainabilityScore() );
        }
        if ( dto.getNutritionalValue() != null ) {
            if ( product.getNutritionalValue() == null ) {
                product.setNutritionalValue( new NutritionalValue() );
            }
            nutritionalValueMapper.updateEntity( dto.getNutritionalValue(), product.getNutritionalValue() );
        }
        if ( product.getDeclaredAllergens() != null ) {
            product.getDeclaredAllergens().clear();
            List<Allergen> list = allergenDTOListToAllergenList( dto.getDeclaredAllergens() );
            if ( list != null ) {
                product.getDeclaredAllergens().addAll( list );
            }
        }
    }

    private Long productCategoryId(Product product) {
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }

    private String productCategoryName(Product product) {
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }

    protected List<Allergen> allergenDTOListToAllergenList(List<AllergenDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Allergen> list1 = new ArrayList<Allergen>( list.size() );
        for ( AllergenDTO allergenDTO : list ) {
            list1.add( allergenMapper.toEntity( allergenDTO ) );
        }

        return list1;
    }
}
