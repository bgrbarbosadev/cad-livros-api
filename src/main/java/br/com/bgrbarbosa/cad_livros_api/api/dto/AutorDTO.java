package br.com.bgrbarbosa.cad_livros_api.api.dto;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record AutorDTO(

    UUID idAutor,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 3, max = 70, message = Messages.FIELD_SIZE_MESSAGE)
    String nameAutor,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 5, max = 200, message = Messages.FIELD_SIZE_MESSAGE)
    String biografiaAutor,

    @NotNull(message = Messages.NOT_NULL)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dtNascAutor,

    @NotBlank(message = Messages.NOT_BLANK)
    @Size(min = 5, max = 50, message = Messages.FIELD_SIZE_MESSAGE)
    String nacionalidadeAutor,

    String fotoAutor,

    List<Livro> livros
) { }
