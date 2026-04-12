package it.unifi.swam.cleanlabel.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private String description;
}