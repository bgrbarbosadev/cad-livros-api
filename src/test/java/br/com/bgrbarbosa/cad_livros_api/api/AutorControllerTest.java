package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/data-autor.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar uma lista paginada de autores com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testFindAllWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/autor")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nameAutor").value("José Silva"))
                .andExpect(jsonPath("$.content[0].biografiaAutor").value("Autor contemporâneo."))
                .andExpect(jsonPath("$.content[0].dtNascAutor").value("12/05/1980"))
                .andExpect(jsonPath("$.content[0].nacionalidadeAutor").value("Brasil"))
                .andExpect(jsonPath("$.content[0].fotoAutor").value("jose_silva.jpg"));

    }

    @Test
    @DisplayName("Deve retornar um autor pelo id com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/autor/{id}", "680cb3a4-8d79-430b-a839-e57afc746aba")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAutor").isNotEmpty())
                .andExpect(jsonPath("$.nameAutor").value("Maria Oliveira"))
                .andExpect(jsonPath("$.biografiaAutor").value("Romancista."))
                .andExpect(jsonPath("$.dtNascAutor").value("23/08/1975"))
                .andExpect(jsonPath("$.nacionalidadeAutor").value("Portugal"))
                .andExpect(jsonPath("$.fotoAutor").value("maria_oliveira.jpg"));
    }

    @Test
    @DisplayName("Deve retornar um resource not found quando id não existir")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithFail() throws Exception {

        mockMvc.perform(get("/api/v1/autor/{id}", "c61eb6b7-005c-482c-8b13-2d1caeb3c50a")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND));
    }

    @Test
    @DisplayName("Deve inserir um novo autor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void insertWithSucess() throws Exception {

        Autor autor = new Autor(
                        null,
                        "Ana Paula",
                        "Escritora de ficção científica.",
                        java.time.LocalDate.of(1990, 3, 15),
                        "Brasil",
                        "ana_paula.jpg",
                        null);

        mockMvc.perform(post("/api/v1/autor")
                .contentType(APPLICATION_JSON)
                .content(toJson(autor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAutor").isNotEmpty());

    }

    @Test
    @DisplayName("Deve retornar erro de validação ao tentar inserir um autor com dados inválidos")
    @WithMockUser(roles = "ADMIN")
    void insertWithNotSucess() throws Exception {

        Autor autor = new Autor(
                null,
                "A",
                "E.",
                java.time.LocalDate.of(1990, 3, 15),
                "Brasil",
                "ana_paula.jpg",
                null);

        mockMvc.perform(post("/api/v1/autor")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(autor)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Validation exception"))
                .andExpect(jsonPath("$.message").value(Messages.VALIDATION_MESSAGE))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'nameAutor')].message").value("Field must contain between 3 and 70 characters"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'biografiaAutor')].message").value("Field must contain between 5 and 200 characters"));
    }

    @Test
    @DisplayName("Deve atualizar um autor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void updateWithSucess() throws Exception {

        Autor autor = new Autor(
                UUID.fromString("680cb3a4-8d79-430b-a839-e57afc746aba"),
                "Maria Oliveira - alterado",
                "Escritora de ficção científica.",
                java.time.LocalDate.of(1975, 3, 15),
                "Portugal-alterado",
                "maria_oliveira.jpg",
                null);

        mockMvc.perform(put("/api/v1/autor")
                .contentType(APPLICATION_JSON)
                .content(toJson(autor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAutor").isNotEmpty())
                .andExpect(jsonPath("$.nameAutor").value("Maria Oliveira - alterado"))
                .andExpect(jsonPath("$.nacionalidadeAutor").value("Portugal-alterado"))
                .andExpect(jsonPath("$.dtNascAutor").value("15/03/1975"));

    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao atualizar um autor com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void update() throws Exception {

        Autor autor = new Autor(
                UUID.fromString("78014f49-970c-404f-a2bf-a031f95b6964"),
                "Maria Oliveira - alterado",
                "Escritora de ficção científica.",
                java.time.LocalDate.of(1975, 3, 15),
                "Portugal-alterado",
                "maria_oliveira.jpg",
                null);

        mockMvc.perform(put("/api/v1/autor")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(autor)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

    }


    @Test
    @DisplayName("Deve deletar um autor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteAutorWithSucess() throws Exception {

        UUID id = UUID.fromString("d45f77d8-7254-4bfe-b2fb-206a7c3c27c9");

        mockMvc.perform(delete("/api/v1/autor/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao atualizar um autor com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void deleteAutorWithNotSucess() throws Exception {

        UUID id = UUID.fromString("f0a7cf78-02f2-42ac-a2b2-3e5087f29beb");

        mockMvc.perform(delete("/api/v1/autor/{id}", id)
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