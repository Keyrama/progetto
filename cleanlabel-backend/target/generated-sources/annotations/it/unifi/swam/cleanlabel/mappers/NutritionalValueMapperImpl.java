package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.NutritionalValueDTO;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-01T18:17:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class NutritionalValueMapperImpl implements NutritionalValueMapper {

    @Override
    public NutritionalValueDTO toDTO(NutritionalValue nv) {
        if ( nv == null ) {
            return null;
        }

        NutritionalValueDTO.NutritionalValueDTOBuilder nutritionalValueDTO = NutritionalValueDTO.builder();

        nutritionalValueDTO.id( nv.getId() );
        nutritionalValueDTO.calories( nv.getCalories() );
        nutritionalValueDTO.proteins( nv.getProteins() );
        nutritionalValueDTO.carbohydrates( nv.getCarbohydrates() );
        nutritionalValueDTO.sugars( nv.getSugars() );
        nutritionalValueDTO.fats( nv.getFats() );
        nutritionalValueDTO.saturatedFats( nv.getSaturatedFats() );
        nutritionalValueDTO.salt( nv.getSalt() );
        nutritionalValueDTO.fiber( nv.getFiber() );

        return nutritionalValueDTO.build();
    }

    @Override
    public NutritionalValue toEntity(NutritionalValueDTO dto) {
        if ( dto == null ) {
            return null;
        }

        NutritionalValue nutritionalValue = new NutritionalValue();

        nutritionalValue.setId( dto.getId() );
        nutritionalValue.setCalories( dto.getCalories() );
        nutritionalValue.setProteins( dto.getProteins() );
        nutritionalValue.setCarbohydrates( dto.getCarbohydrates() );
        nutritionalValue.setSugars( dto.getSugars() );
        nutritionalValue.setFats( dto.getFats() );
        nutritionalValue.setSaturatedFats( dto.getSaturatedFats() );
        nutritionalValue.setSalt( dto.getSalt() );
        nutritionalValue.setFiber( dto.getFiber() );

        return nutritionalValue;
    }
}
