package br.com.bgrbarbosa.cad_livros_api.api.dto;




import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID uuid,

        @NotBlank(message = Messages.NOT_BLANK)
        @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
        String firstName,

        @NotBlank(message = Messages.NOT_BLANK)
        @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
        String lastName,

        @Email(message = Messages.EMAIL_VALID)
        String email,

        @NotBlank(message = Messages.NOT_BLANK)
        @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
        String password,

        @NotNull(message = Messages.NOT_NULL)
        Set<Role> roles
) { }
