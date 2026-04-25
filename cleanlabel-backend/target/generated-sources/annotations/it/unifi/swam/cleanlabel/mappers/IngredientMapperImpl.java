package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.model.Ingredient;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-25T18:35:23+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class IngredientMapperImpl implements IngredientMapper {

    @Autowired
    private AllergenMapper allergenMapper;

    @Override
    public IngredientDTO toDTO(Ingredient ingredient) {
        if ( ingredient == null ) {
            return null;
        }

        IngredientDTO.IngredientDTOBuilder ingredientDTO = IngredientDTO.builder();

        ingredientDTO.id( ingredient.getId() );
        ingredientDTO.name( ingredient.getName() );
        ingredientDTO.additiveCode( ingredient.getAdditiveCode() );
        ingredientDTO.description( ingredient.getDescription() );
        ingredientDTO.artificial( ingredient.isArtificial() );
        ingredientDTO.riskLevel( ingredient.getRiskLevel() );
        ingredientDTO.allergens( allergenMapper.toDTOList( ingredient.getAllergens() ) );

        return ingredientDTO.build();
    }

    @Override
    public List<IngredientDTO> toDTOList(List<Ingredient> ingredients) {
        if ( ingredients == null ) {
            return null;
        }

        List<IngredientDTO> list = new ArrayList<IngredientDTO>( ingredients.size() );
        for ( Ingredient ingredient : ingredients ) {
            list.add( toDTO( ingredient ) );
        }

        return list;
    }

    @Override
    public Ingredient toEntity(IngredientDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Ingredient ingredient = new Ingredient();

        ingredient.setId( dto.getId() );
        ingredient.setName( dto.getName() );
        ingredient.setAdditiveCode( dto.getAdditiveCode() );
        ingredient.setDescription( dto.getDescription() );
        ingredient.setArtificial( dto.isArtificial() );
        ingredient.setRiskLevel( dto.getRiskLevel() );

        return ingredient;
    }

    @Override
    public void updateEntity(IngredientDTO dto, Ingredient ingredient) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            ingredient.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            ingredient.setName( dto.getName() );
        }
        if ( dto.getAdditiveCode() != null ) {
            ingredient.setAdditiveCode( dto.getAdditiveCode() );
        }
        if ( dto.getDescription() != null ) {
            ingredient.setDescription( dto.getDescription() );
        }
        ingredient.setArtificial( dto.isArtificial() );
        if ( dto.getRiskLevel() != null ) {
            ingredient.setRiskLevel( dto.getRiskLevel() );
        }
    }
}
