package br.com.bgrbarbosa.cad_livros_api.business;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AutorService {

    Autor insert(Autor entity);
    Page<Autor> findAll(Pageable page);
    List<Autor> findAll();
    Autor findById(UUID id);
    void delete(UUID id);
    Autor update(Autor entity);
}
