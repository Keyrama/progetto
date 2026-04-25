package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.NutritionalValueDTO;
import it.unifi.swam.cleanlabel.model.NutritionalValue;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-25T18:35:23+0200",
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

    @Override
    public void updateEntity(NutritionalValueDTO dto, NutritionalValue nv) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            nv.setId( dto.getId() );
        }
        if ( dto.getCalories() != null ) {
            nv.setCalories( dto.getCalories() );
        }
        if ( dto.getProteins() != null ) {
            nv.setProteins( dto.getProteins() );
        }
        if ( dto.getCarbohydrates() != null ) {
            nv.setCarbohydrates( dto.getCarbohydrates() );
        }
        if ( dto.getSugars() != null ) {
            nv.setSugars( dto.getSugars() );
        }
        if ( dto.getFats() != null ) {
            nv.setFats( dto.getFats() );
        }
        if ( dto.getSaturatedFats() != null ) {
            nv.setSaturatedFats( dto.getSaturatedFats() );
        }
        if ( dto.getSalt() != null ) {
            nv.setSalt( dto.getSalt() );
        }
        if ( dto.getFiber() != null ) {
            nv.setFiber( dto.getFiber() );
        }
    }
}
