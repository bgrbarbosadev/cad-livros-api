package br.com.bgrbarbosa.cad_livros_api.business.impl;


import br.com.bgrbarbosa.cad_livros_api.business.GeneroService;
import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository repository;

    @Override
    public Genero insert(Genero entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Genero> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public List<Genero> findAll() {
        return repository.findAll();
    }

    @Override
    public Genero findById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND)
        );
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }

        repository.deleteById(id);
    }

    @Override
    public Genero update(Genero entity) {
        if (!repository.existsById(entity.getIdGenero())) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }
        return repository.save(entity);
    }
}
