package br.com.bgrbarbosa.cad_livros_api.infraestruture.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_editora")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idEditora;

    @Column
    private String cnpjEditora;

    @Column
    private String razaoSocialEditora;

    @Column
    private String telEditora;

    @OneToMany(mappedBy = "editoraLivro")
    @JsonIgnore
    private List<Livro> livros;
}
