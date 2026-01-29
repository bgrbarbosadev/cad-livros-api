package br.com.bgrbarbosa.cad_livros_api.api.dto;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record GeneroDTO(

    UUID idGenero,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 5, max = 200, message = Messages.FIELD_SIZE_MESSAGE)
    String descGenero,

    List<Livro> livros
) { }
