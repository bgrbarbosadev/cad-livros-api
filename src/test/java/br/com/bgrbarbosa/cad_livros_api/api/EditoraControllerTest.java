package br.com.bgrbarbosa.cad_livros_api.api;


import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
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
@Sql(scripts = "/data-editora.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EditoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar uma lista paginada de editoras com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testFindAllWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/editora")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].cnpjEditora").value("12.345.678/0001-01"))
                .andExpect(jsonPath("$.content[0].razaoSocialEditora").value("Editora Horizonte Digital Ltda"))
                .andExpect(jsonPath("$.content[0].telEditora").value("(11) 4002-8922"));
    }

    @Test
    @DisplayName("Deve retornar uma editora pelo id com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/editora/{id}", "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEditora").isNotEmpty())
                .andExpect(jsonPath("$.cnpjEditora").value("12.345.678/0001-01"))
                .andExpect(jsonPath("$.razaoSocialEditora").value("Editora Horizonte Digital Ltda"))
                .andExpect(jsonPath("$.telEditora").value("(11) 4002-8922"));
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
    @DisplayName("Deve inserir uma nova editora com sucesso")
    @WithMockUser(roles = "ADMIN")
    void insertWithSucess() throws Exception {

        Editora editora = new Editora(
                null,
                "58264487000108",
                "Editora Nova Era S.A",
                "2130031234",
                null
        );

        mockMvc.perform(post("/api/v1/editora")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(editora)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEditora").isNotEmpty())
                .andExpect(jsonPath("$.cnpjEditora").value("58264487000108"))
                .andExpect(jsonPath("$.razaoSocialEditora").value("Editora Nova Era S.A"))
                .andExpect(jsonPath("$.telEditora").value("2130031234"));

    }

    @Test
    @DisplayName("Deve retornar erro de validação ao tentar inserir uma editora com dados inválidos")
    @WithMockUser(roles = "ADMIN")
    void insertWithNotSucess() throws Exception {

        Editora editora = new Editora(
                null,
                "582",
                "Ed",
                "2130031234",
                null
        );

        mockMvc.perform(post("/api/v1/editora")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(editora)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Validation exception"))
                .andExpect(jsonPath("$.message").value(Messages.VALIDATION_MESSAGE))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'cnpjEditora')].message").value(Messages.INVALID_CNPJ))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'razaoSocialEditora')].message").value("Field must contain between 5 and 200 characters"));
    }

    @Test
    @DisplayName("Deve atualizar um autor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void updateWithSucess() throws Exception {

        Editora editora = new Editora(
                UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"),
                "58264487000108",
                "Editora Alterada",
                "1140028922",
                null
        );

        mockMvc.perform(put("/api/v1/editora")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(editora)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEditora").value("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"))
                .andExpect(jsonPath("$.cnpjEditora").value("58264487000108"))
                .andExpect(jsonPath("$.razaoSocialEditora").value("Editora Alterada"))
                .andExpect(jsonPath("$.telEditora").value("1140028922"));

    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao atualizar uma editora com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void updateNotSucess() throws Exception {

        Editora editora = new Editora(
                UUID.fromString("a1bc0f4f-2f8c-44fb-9d19-93be8ac20502"),
                "58264487000108",
                "Editora Alterada",
                "1140028922",
                null
        );

        mockMvc.perform(put("/api/v1/editora")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(editora)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

    }

    @Test
    @DisplayName("Deve deletar uma editora com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteEditoraWithSucess() throws Exception {

        UUID id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

        mockMvc.perform(delete("/api/v1/editora/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao deletar uma editora com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void deleteAutorWithNotSucess() throws Exception {

        UUID id = UUID.fromString("f0a7cf78-02f2-42ac-a2b2-3e5087f29beb");

        mockMvc.perform(delete("/api/v1/editora/{id}", id)
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