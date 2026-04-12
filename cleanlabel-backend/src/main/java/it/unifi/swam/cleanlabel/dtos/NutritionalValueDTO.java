package it.unifi.swam.cleanlabel.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NutritionalValueDTO {

    private Long id;

    @NotNull @PositiveOrZero
    private Double calories;

    @NotNull @PositiveOrZero
    private Double proteins;

    @NotNull @PositiveOrZero
    private Double carbohydrates;

    @NotNull @PositiveOrZero
    private Double sugars;

    @NotNull @PositiveOrZero
    private Double fats;

    @NotNull @PositiveOrZero
    private Double saturatedFats;

    @NotNull @PositiveOrZero
    private Double salt;

    @PositiveOrZero
    private Double fiber;
}