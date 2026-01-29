package br.com.bgrbarbosa.cad_livros_api.business;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EditoraService {

    Editora insert(Editora entity);
    Page<Editora> findAll(Pageable page);
    List<Editora> findAll();
    Editora findById(UUID id);
    void delete(UUID id);
    Editora update(Editora entity);
}
