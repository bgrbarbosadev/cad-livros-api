package br.com.bgrbarbosa.cad_livros_api.infraestruture.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_autor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idAutor;

    @Column
    private String nameAutor;

    @Column
    private String biografiaAutor;

    @Column
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtNascAutor;

    @Column
    private String nacionalidadeAutor;

    @Column
    private String fotoAutor;

    @OneToMany(mappedBy = "autorLivro")
    @JsonIgnore
    private List<Livro> livros;
}
