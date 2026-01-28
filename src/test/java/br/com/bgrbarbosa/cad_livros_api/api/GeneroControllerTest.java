package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
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
@Sql(scripts = "/data-genero.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar uma lista paginada de generos com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testFindAllWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/genero")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].idGenero").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.content[0].descGenero").value("Ficção Científica"));
    }

    @Test
    @DisplayName("Deve retornar um genero pelo id com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/genero/{id}", "550e8400-e29b-41d4-a716-446655440000")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idGenero").isNotEmpty())
                .andExpect(jsonPath("$.idGenero").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.descGenero").value("Ficção Científica"));
    }

    @Test
    @DisplayName("Deve retornar um resource not found quando id não existir")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithFail() throws Exception {

        mockMvc.perform(get("/api/v1/genero/{id}", "c61eb6b7-005c-482c-8b13-2d1caeb3c50a")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND));
    }

    @Test
    @DisplayName("Deve inserir um novo genero com sucesso")
    @WithMockUser(roles = "ADMIN")
    void insertWithSucess() throws Exception {

        Genero genero = new Genero(
                null,
                "Romance",
                null
        );

        mockMvc.perform(post("/api/v1/genero")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(genero)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idGenero").isNotEmpty())
                .andExpect(jsonPath("$.descGenero").value("Romance"));
    }

    @Test
    @DisplayName("Deve retornar erro de validação ao tentar inserir um genero com dados inválidos")
    @WithMockUser(roles = "ADMIN")
    void insertWithNotSucess() throws Exception {

        Genero genero = new Genero(
                null,
                "R",
                null
        );

        mockMvc.perform(post("/api/v1/genero")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(genero)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Validation exception"))
                .andExpect(jsonPath("$.message").value(Messages.VALIDATION_MESSAGE))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'descGenero')].message").value("Field must contain between 5 and 200 characters"));
    }

    @Test
    @DisplayName("Deve atualizar um genero com sucesso")
    @WithMockUser(roles = "ADMIN")
    void updateWithSucess() throws Exception {

        Genero genero = new Genero(
                UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8"),
                "Fantasia alterado",
                null
        );

        mockMvc.perform(put("/api/v1/genero")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(genero)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idGenero").value("6ba7b810-9dad-11d1-80b4-00c04fd430c8"))
                .andExpect(jsonPath("$.descGenero").value("Fantasia alterado"));
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao atualizar uma editora com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void updateNotSucess() throws Exception {

        Genero genero = new Genero(
                UUID.fromString("bd2403dc-9dfd-4455-af3e-d73c6a321102"),
                "Inexistente",
                null
        );

        mockMvc.perform(put("/api/v1/genero")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(genero)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

    }

    @Test
    @DisplayName("Deve deletar um genero com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteGeneroWithSucess() throws Exception {

        UUID id = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");

        mockMvc.perform(delete("/api/v1/genero/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao deletar uma editora com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void deleteAutorWithNotSucess() throws Exception {

        UUID id = UUID.fromString("f0a7cf78-02f2-42ac-a2b2-3e5087f29beb");

        mockMvc.perform(delete("/api/v1/genero/{id}", id)
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