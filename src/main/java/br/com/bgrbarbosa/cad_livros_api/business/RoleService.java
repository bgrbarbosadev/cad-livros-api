package br.com.bgrbarbosa.cad_livros_api.business;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.RoleException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    Role insert(Role role) throws RoleException;

    List<Role> findAll(Pageable page);

    Role findById(UUID uuid);

    void delete(UUID uuid);

    Role update(Role role);
}
