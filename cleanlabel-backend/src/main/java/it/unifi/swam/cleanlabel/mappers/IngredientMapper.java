package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.IngredientDTO;
import it.unifi.swam.cleanlabel.model.Ingredient;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AllergenMapper.class})
public interface IngredientMapper {

    IngredientDTO toDTO(Ingredient ingredient);

    List<IngredientDTO> toDTOList(List<Ingredient> ingredients);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "allergens", ignore = true)
    Ingredient toEntity(IngredientDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "allergens", ignore = true)
    void updateEntity(IngredientDTO dto, @MappingTarget Ingredient ingredient);
}