package it.unifi.swam.cleanlabel.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllergenDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
}