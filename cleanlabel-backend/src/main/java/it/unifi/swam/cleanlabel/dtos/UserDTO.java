package it.unifi.swam.cleanlabel.dtos;

import it.unifi.swam.cleanlabel.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long id;

    @NotBlank
    private String username;

    @NotBlank @Email
    private String email;

    @NotNull
    private User.Role role;
}