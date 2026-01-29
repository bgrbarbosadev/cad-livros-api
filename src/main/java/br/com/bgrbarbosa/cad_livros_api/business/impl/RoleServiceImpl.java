package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.business.RoleService;
import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.RoleException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public Role insert(Role role) throws RoleException {
        Role aux = role;
        if (repository.existsByAuthority(aux.getAuthority())) {
            throw new RoleException(Messages.EXISTING_ROLE);
        }
        return repository.save(role);
    }

    @Override
    public List<Role> findAll(Pageable page) {
        return repository.findAll();
    }

    @Override
    public Role findById(UUID uuid) {
        return repository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND + uuid)
        );
    }

    @Override
    public void delete(UUID uuid) {
        if (!repository.existsById(uuid)) {
            throw new ResourceNotFoundException("ID not found: " + uuid);
        }
        repository.deleteById(uuid);
    }

    @Override
    public Role update(Role role) {
        Role aux = repository.findById(role.getUuid()).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found!"));
        aux.setAuthority(role.getAuthority());
        return repository.save(aux);
    }

}
