package br.com.bgrbarbosa.cad_livros_api.business;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GeneroService {

    Genero insert(Genero entity);
    Page<Genero> findAll(Pageable page);
    List<Genero> findAll();
    Genero findById(UUID id);
    void delete(UUID id);
    Genero update(Genero entity);
}
