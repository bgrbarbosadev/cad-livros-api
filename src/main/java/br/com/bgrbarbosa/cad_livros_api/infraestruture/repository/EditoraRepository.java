package br.com.bgrbarbosa.cad_livros_api.infraestruture.repository;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EditoraRepository extends JpaRepository<Editora, UUID> {
}
