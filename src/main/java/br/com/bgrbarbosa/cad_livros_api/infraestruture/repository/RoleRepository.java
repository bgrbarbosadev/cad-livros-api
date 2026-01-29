package br.com.bgrbarbosa.cad_livros_api.infraestruture.repository;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByAuthority(String authority);
}
