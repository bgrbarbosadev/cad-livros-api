package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import br.com.bgrbarbosa.cad_livros_api.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/data-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Test
    @DisplayName("Deve retornar uma lista paginada de Usuários com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void testFindAllWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/user")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].uuid").value("31b87395-4cd2-44b4-b34c-23c025d87397"))
                .andExpect(jsonPath("$.content[0].firstName").value("Admin"))
                .andExpect(jsonPath("$.content[0].lastName").value("Admin"))
                .andExpect(jsonPath("$.content[0].email").value("admin@mail.com"))
                .andExpect(jsonPath("$.content[0].password").value("$2a$12$8K/5PsoF0Xet1GkAShqBLeMdJ6.EtXTU6Tn.SZpridhx0H3w8H.2K"))
                .andExpect(jsonPath("$.content[0].roles[0].authority").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve retornar um user pelo id com sucesso")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithSucess() throws Exception {

        mockMvc.perform(get("/api/v1/user/{id}", "e774f0fa-ea64-45f0-80ff-77dd24258660")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value("e774f0fa-ea64-45f0-80ff-77dd24258660"))
                .andExpect(jsonPath("$.firstName").value("User"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("user@mail.com"))
                .andExpect(jsonPath("$.password").value("$2a$12$8K/5PsoF0Xet1GkAShqBLeMdJ6.EtXTU6Tn.SZpridhx0H3w8H.2K"))
                .andExpect(jsonPath("$.roles[0].authority").value("USER"));
    }

    @Test
    @DisplayName("Deve retornar um resource not found quando id não existir")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void findByIdWithFail() throws Exception {

        mockMvc.perform(get("/api/v1/user/{id}", "c61eb6b7-005c-482c-8b13-2d1caeb3c50a")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND+"c61eb6b7-005c-482c-8b13-2d1caeb3c50a"));
    }

    @Test
    @DisplayName("Deve inserir um novo usuario com sucesso")
    @WithMockUser(roles = "ADMIN")
    void insertWithSucess() throws Exception {

        Role userRole = new Role();
        userRole.setUuid(UUID.fromString("cfcee1fc-51e6-43e4-8ffc-5c5f54e2f6e1"));
        userRole.setAuthority("USER");

        User user = new User(null, "New", "User", "new@mail.com", passwordEncoder.encode("123456"), Set.of(userRole));

        mockMvc.perform(post("/api/v1/user")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.roles[0].authority").value("USER"));
    }

    @Test
    @DisplayName("Deve retornar erro de validação ao tentar inserir um usuario com dados inválidos")
    @WithMockUser(roles = "ADMIN")
    void insertWithNotSucess() throws Exception {

        Role userRole = new Role();
        userRole.setUuid(UUID.fromString("31b87395-4cd2-44b4-b34c-23c025d87397"));
        userRole.setAuthority("USER");

        User user = new User(null, "Ne", "U", "admin@mail.com", passwordEncoder.encode("123456"), Set.of(userRole));

        mockMvc.perform(post("/api/v1/user")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Validation exception"))
                .andExpect(jsonPath("$.message").value(Messages.VALIDATION_MESSAGE))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'firstName')].message").value("Field must contain between 3 and 70 characters"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'lastName')].message").value("Field must contain between 3 and 70 characters"));
    }

    @Test
    @DisplayName("Deve atualizar um usuario com sucesso")
    @WithMockUser(roles = "ADMIN")
    void updateWithSucess() throws Exception {

        Role userRole = new Role();
        userRole.setUuid(UUID.fromString("cfcee1fc-51e6-43e4-8ffc-5c5f54e2f6e1"));
        userRole.setAuthority("USER");

        User user = new User(UUID.fromString("e774f0fa-ea64-45f0-80ff-77dd24258660"), "User - UPDATE", "User - UPDATE", "user@mail.com", "$2a$12$8K/5PsoF0Xet1GkAShqBLeMdJ6.EtXTU6Tn.SZpridhx0H3w8H.2K", Set.of(userRole));

        mockMvc.perform(put("/api/v1/user")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("User - UPDATE"))
                .andExpect(jsonPath("$.lastName").value("User - UPDATE"))
                .andExpect(jsonPath("$.email").value("user@mail.com"))
                .andExpect(jsonPath("$.password").isNotEmpty())
                .andExpect(jsonPath("$.roles[0].authority").value("USER"));
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao atualizar um usuario com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void updateNotSucess() throws Exception {

        Role userRole = new Role();
        userRole.setUuid(UUID.fromString("cfcee1fc-51e6-43e4-8ffc-5c5f54e2f6e1"));
        userRole.setAuthority("USER");

        User user = new User(UUID.fromString("7e3e7312-fd30-4a1b-8965-2f709f4cf62f"), "User - Not Sucess", "User - UPDATE", "user@mail.com", "$2a$12$8K/5PsoF0Xet1GkAShqBLeMdJ6.EtXTU6Tn.SZpridhx0H3w8H.2K", Set.of(userRole));

        mockMvc.perform(put("/api/v1/user")
                        .contentType(APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.error").value(Messages.RESOURCE_NOT_FOUND))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

    }

    @Test
    @DisplayName("Deve deletar um usuario com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deleteUserWithSucess() throws Exception {

        UUID id = UUID.fromString("e774f0fa-ea64-45f0-80ff-77dd24258660");

        mockMvc.perform(delete("/api/v1/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar um Resource not found ao deletar um usuario com id inexistente")
    @WithMockUser(roles = "ADMIN")
    void deleteUserWithNotSucess() throws Exception {

        UUID id = UUID.fromString("f0a7cf78-02f2-42ac-a2b2-3e5087f29beb");

        mockMvc.perform(delete("/api/v1/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Messages.RESOURCE_NOT_FOUND + id))
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