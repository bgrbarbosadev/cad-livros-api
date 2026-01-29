package br.com.bgrbarbosa.cad_livros_api.api.dto;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;
import java.util.UUID;

public record EditoraDTO(

    UUID idEditora,

    @NotBlank(message = Messages.NOT_BLANK)
    @CNPJ(message = Messages.INVALID_CNPJ)
    String cnpjEditora,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 5, max = 200, message = Messages.FIELD_SIZE_MESSAGE)
    String razaoSocialEditora,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 10, max = 13, message = Messages.FIELD_SIZE_MESSAGE)
    String telEditora,

    List<Livro> livros
) { }
