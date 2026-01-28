package br.com.bgrbarbosa.cad_livros_api.business;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.UserException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User insert(User user) throws UserException;

    List<User> findAll(Pageable page);

    User findById(UUID uuid);

    void delete(UUID uuid);

    User update(User user);

    User loadUserByUsername(String email) throws UsernameNotFoundException;
}
