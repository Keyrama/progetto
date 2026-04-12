package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.AllergenDTO;
import it.unifi.swam.cleanlabel.model.Allergen;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-12T16:35:55+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class AllergenMapperImpl implements AllergenMapper {

    @Override
    public AllergenDTO toDTO(Allergen allergen) {
        if ( allergen == null ) {
            return null;
        }

        AllergenDTO.AllergenDTOBuilder allergenDTO = AllergenDTO.builder();

        allergenDTO.id( allergen.getId() );
        allergenDTO.name( allergen.getName() );
        allergenDTO.code( allergen.getCode() );
        allergenDTO.description( allergen.getDescription() );

        return allergenDTO.build();
    }

    @Override
    public List<AllergenDTO> toDTOList(List<Allergen> allergens) {
        if ( allergens == null ) {
            return null;
        }

        List<AllergenDTO> list = new ArrayList<AllergenDTO>( allergens.size() );
        for ( Allergen allergen : allergens ) {
            list.add( toDTO( allergen ) );
        }

        return list;
    }

    @Override
    public Allergen toEntity(AllergenDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Allergen allergen = new Allergen();

        allergen.setId( dto.getId() );
        allergen.setName( dto.getName() );
        allergen.setCode( dto.getCode() );
        allergen.setDescription( dto.getDescription() );

        return allergen;
    }
}
