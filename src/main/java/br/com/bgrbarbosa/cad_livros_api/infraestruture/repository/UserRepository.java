package br.com.bgrbarbosa.cad_livros_api.infraestruture.repository;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
