package br.com.bgrbarbosa.cad_livros_api.api.dto;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LivroDTO(

    UUID idLivro,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
    String tituloLivro,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 5, max = 20, message = Messages.FIELD_SIZE_MESSAGE)
    String isbn,

    @NotNull(message = Messages.NOT_NULL)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate anoPublicacao,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 5, max = 50, message = Messages.FIELD_SIZE_MESSAGE)
    String edicaoLivro,

    @NotNull(message = Messages.NOT_NULL)
    BigDecimal precoLivro,

    @NotNull(message = Messages.NOT_NULL)
    Autor autorLivro,

    @NotNull(message = Messages.NOT_NULL)
    Genero generoLivro,

    @NotNull(message = Messages.NOT_NULL)
    Editora editoraLivro
) { }
