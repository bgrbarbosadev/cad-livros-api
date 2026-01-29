package br.com.bgrbarbosa.cad_livros_api.infraestruture.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_genero")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idGenero;

    @Column
    private String descGenero;

    @OneToMany(mappedBy = "generoLivro", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Livro> livros;
}
