package br.com.bgrbarbosa.cad_livros_api.business.impl;


import br.com.bgrbarbosa.cad_livros_api.business.LivroService;
import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository repository;

    @Override
    public Livro insert(Livro entity) {
        Livro result = repository.save(entity);
        return result;
    }

    @Override
    public Page<Livro> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public List<Livro> findAll() {
        return repository.findAll();
    }

    @Override
    public Livro findById(UUID id) {
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
    public Livro update(Livro entity) {
        if (!repository.existsById(entity.getIdLivro())) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND);
        }
        Livro result = repository.save(entity);
        return result;
    }
}
