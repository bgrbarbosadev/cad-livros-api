package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/data-livro.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar uma lista paginada de livros com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testFindAllWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/livro")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].idLivro").value("d69c2654-c170-4975-9379-e177e9faec25"))
                .andExpect(jsonPath("$.content[0].tituloLivro").value("A Arte da Programação"));
    }

    @Test
    @DisplayName("Deve retornar um livro pelo id com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/livro/{id}", "d69c2654-c170-4975-9379-e177e9faec25")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLivro").isNotEmpty())
                .andExpect(jsonPath("$.idLivro").value("d69c2654-c170-4975-9379-e177e9faec25"))
                .andExpect(jsonPath("$.tituloLivro").value("A Arte da Programação"));
    }

    @Test
    @DisplayName("Deve retornar um resource not found quando id não existir")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithFail() throws Exception {

        mockMvc.perform(get("/api/v1/livro/{id}", "c61eb6b7-005c-482c-8b13-2d1caeb3c50a")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND));
    }

    @Test
    @DisplayName("Deve inserir um novo livro com sucesso")
    @WithMockUser(roles = "ADMIN")
    void insertWithSucess() throws Exception {

        Livro livro = new Livro(
            UUID.fromString("d69c2654-c170-4975-9379-e177e9faec25"),
            "A Arte da Programação",
            "978-0201896831",
            java.time.LocalDate.of(1990, 3, 15),
            "Edicao 1",
            new BigDecimal("150.00"),
            new Autor(UUID.fromString("d45f77d8-7254-4bfe-b2fb-206a7c3c27c9"), "José Silva", "Autor contemporâneo.", java.time.LocalDate.of(1990, 3, 15), "Brasil", "jose_silva.jpg", null),
            new Genero(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ficção Científica", null),
            new Editora(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), "33778274000165", "Editora Horizonte Digital Ltda", "1140028922", null)
        );

        mockMvc.perform(post("/api/v1/livro")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(livro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idLivro").isNotEmpty())
                .andExpect(jsonPath("$.tituloLivro").value("A Arte da Programação"))
                .andExpect(jsonPath("$.isbn").value("978-0201896831"))
                .andExpect(jsonPath("$.edicaoLivro").value("Edicao 1"));
        }

    @Test
    @DisplayName("Deve retornar erro de validação ao tentar inserir um livro com dados inválidos")
    @WithMockUser(roles = "ADMIN")
    void insertWithNotSucess() throws Exception {

        Livro livro = new Livro(
                UUID.fromString("d69c2654-c170-4975-9379-e177e9faec25"),
                "A",
                "978-0201896831",
                java.time.LocalDate.of(1990, 3, 15),
                "1",
                new BigDecimal("150.00"),
                new Autor(UUID.fromString("d45f77d8-7254-4bfe-b2fb-206a7c3c27c9"), "José Silva", "Autor contemporâneo.", java.time.LocalDate.of(1990, 3, 15), "Brasil", "jose_silva.jpg", null),
                new Genero(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ficção Científica", null),
                new Editora(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), "33778274000165", "Editora Horizonte Digital Ltda", "1140028922", null)
        );

        mockMvc.perform(post("/api/v1/livro")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(livro)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Validation exception"))
                .andExpect(jsonPath("$.message").value(Messages.VALIDATION_MESSAGE))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'tituloLivro')].message").value("Field must contain between 3 and 70 characters"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'edicaoLivro')].message").value("Field must contain between 5 and 50 characters"));
    }


    @Test
    @DisplayName("Deve atualizar um livro com sucesso")
    @WithMockUser(roles = "ADMIN")
    void updateWithSucess() throws Exception {

        Livro livro = new Livro(
                UUID.fromString("d69c2654-c170-4975-9379-e177e9faec25"),
                "Livro Alterado",
                "978-0201896831",
                java.time.LocalDate.of(1990, 3, 15),
                "Edicao 2",
                new BigDecimal("150.00"),
                new Autor(UUID.fromString("d45f77d8-7254-4bfe-b2fb-206a7c3c27c9"), "José Silva", "Autor contemporâneo.", java.time.LocalDate.of(1990, 3, 15), "Brasil", "jose_silva.jpg", null),
                new Genero(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ficção Científica", null),
                new Editora(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), "33778274000165", "Editora Horizonte Digital Ltda", "1140028922", null)
        );

        mockMvc.perform(put("/api/v1/livro")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(livro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idLivro").value("d69c2654-c170-4975-9379-e177e9faec25"))
                .andExpect(jsonPath("$.tituloLivro").value("Livro Alterado"));
    }


    @Test
    @DisplayName("Deve retornar um Resource not found ao atualizar um livro com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void updateNotSucess() throws Exception {

        Livro livro = new Livro(
                UUID.fromString("4921ffc0-f3b1-482f-b8f2-06bba58ef687"),
                "Livro Alterado",
                "978-0201896831",
                java.time.LocalDate.of(1990, 3, 15),
                "Edicao 2",
                new BigDecimal("150.00"),
                new Autor(UUID.fromString("d45f77d8-7254-4bfe-b2fb-206a7c3c27c9"), "José Silva", "Autor contemporâneo.", java.time.LocalDate.of(1990, 3, 15), "Brasil", "jose_silva.jpg", null),
                new Genero(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Ficção Científica", null),
                new Editora(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"), "33778274000165", "Editora Horizonte Digital Ltda", "1140028922", null)
        );

        mockMvc.perform(put("/api/v1/livro")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(livro)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        }

    @Test
    @DisplayName("Deve deletar um livro com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteLivroWithSucess() throws Exception {

        UUID id = UUID.fromString("d69c2654-c170-4975-9379-e177e9faec25");

        mockMvc.perform(delete("/api/v1/livro/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao deletar um livro com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void deleteLivroWithNotSucess() throws Exception {

        UUID id = UUID.fromString("f0a7cf78-02f2-42ac-a2b2-3e5087f29beb");

        mockMvc.perform(delete("/api/v1/livro/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND))
            .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

    private String toJson(final Object object) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(object);
        } catch (final Exception e) {
            throw new Exception("Error to convert object to json", e);
        }
    }

}