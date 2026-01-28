package br.com.bgrbarbosa.cad_livros_api.business;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LivroService {

    Livro insert(Livro entity);
    Page<Livro> findAll(Pageable page);
    List<Livro> findAll();
    Livro findById(UUID id);
    void delete(UUID id);
    Livro update(Livro entity);
}
