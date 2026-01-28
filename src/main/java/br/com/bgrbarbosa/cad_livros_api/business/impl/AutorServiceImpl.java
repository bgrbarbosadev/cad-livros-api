package br.com.bgrbarbosa.cad_livros_api.business.impl;


import br.com.bgrbarbosa.cad_livros_api.business.AutorService;
import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository repository;

    @Override
    public Autor insert(Autor entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Autor> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public List<Autor> findAll() {
        return repository.findAll();
    }

    @Override
    public Autor findById(UUID id) {
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
    public Autor update(Autor entity) {
        if (!repository.existsById(entity.getIdAutor())) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }
        return repository.save(entity);
    }
}
