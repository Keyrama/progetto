package it.unifi.swam.cleanlabel.mappers;

import it.unifi.swam.cleanlabel.dtos.AlternativeSuggestionDTO;
import it.unifi.swam.cleanlabel.model.AlternativeSuggestion;

public class AlternativeSuggestionMapper {

    public static AlternativeSuggestionDTO toDTO(AlternativeSuggestion suggestion) {
        if (suggestion == null) return null;

        AlternativeSuggestionDTO dto = new AlternativeSuggestionDTO();
        dto.setId(suggestion.getId());
        dto.setTargetProduct(ProductMapper.toSummaryDTO(suggestion.getTargetProduct()));
        dto.setReason(suggestion.getReason());
        dto.setScoreDelta(suggestion.getScoreDelta());
        return dto;
    }
}