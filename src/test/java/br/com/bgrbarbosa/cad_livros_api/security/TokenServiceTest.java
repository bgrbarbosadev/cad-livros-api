package br.com.bgrbarbosa.cad_livros_api.security;

import br.com.bgrbarbosa.cad_livros_api.api.dto.UserResponseDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private static final String SECRET_TEST = "minha-chave-secreta-de-teste";
    private static final String ISSUER_TEST = "api-livros";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", SECRET_TEST);
        ReflectionTestUtils.setField(tokenService, "withIssuerkey", ISSUER_TEST);
    }

    @Test
    @DisplayName("Deve gerar um token válido quando o usuário for fornecido")
    void generateTokenSuccess() {
        // Arrange
        User user = new User();
        user.setEmail("teste@email.com");
        user.setRoles(Set.of()); // Ajuste conforme sua entidade Role

        // Act
        UserResponseDTO response = tokenService.generateToken(user);

        // Assert
        assertNotNull(response);
        assertEquals("teste@email.com", response.email()); // Ajustado conforme seu DTO
        assertNotNull(response.token());
    }

    @Test
    @DisplayName("Deve validar um token corretamente e retornar o email do subject")
    void validateTokenSuccess() {
        // Arrange
        User user = new User();
        user.setEmail("valido@email.com");
        user.setRoles(Set.of());

        String token = tokenService.generateToken(user).token();

        // Act
        String subject = tokenService.validateToken(token);

        // Assert
        assertEquals("valido@email.com", subject);
    }

    @Test
    @DisplayName("Deve retornar nulo para um token que já expirou")
    void validateTokenExpired() {
        // Arrange

        Algorithm algorithm = Algorithm.HMAC256(SECRET_TEST);
        String tokenExpirado = JWT.create()
                .withIssuer(ISSUER_TEST)
                .withSubject("expirado@teste.com")
                .withExpiresAt(Instant.now().minusSeconds(10))
                .sign(algorithm);

        // Act
        String subject = tokenService.validateToken(tokenExpirado);

        // Assert
        assertNull(subject);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro na criação do algoritmo (Secret nulo)")
    void generateTokenException() {
        // Arrange
        ReflectionTestUtils.setField(tokenService, "secret", null);
        User user = new User();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            tokenService.generateToken(user);
        });
    }
}