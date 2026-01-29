package br.com.bgrbarbosa.cad_livros_api.infraestruture.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_livro")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idLivro;

    @Column
    private String tituloLivro;

    @Column
    private String isbn;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate anoPublicacao;

    @Column
    private String edicaoLivro;

    @Column
    private BigDecimal precoLivro;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autorLivro;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Genero generoLivro;

    @ManyToOne
    @JoinColumn(name = "id_editora")
    private Editora editoraLivro;
}
