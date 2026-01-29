package br.com.bgrbarbosa.cad_livros_api.business.impl;


import br.com.bgrbarbosa.cad_livros_api.business.EditoraService;
import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.EditoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EditoraServiceImpl implements EditoraService {

    private final EditoraRepository repository;

    @Override
    public Editora insert(Editora entity) {
        Editora result = repository.save(entity);
        return result;
    }

    @Override
    public Page<Editora> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public List<Editora> findAll() {
        return repository.findAll();
    }

    @Override
    public Editora findById(UUID id) {
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
    public Editora update(Editora entity) {
        if (!repository.existsById(entity.getIdEditora())) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }
        Editora result = repository.save(entity);
        return result;
    }
}
