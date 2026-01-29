package br.com.bgrbarbosa.cad_livros_api.security;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.UserIdentification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationTest {

    @Mock
    private UserIdentification userIdentification;

    @Test
    @DisplayName("Deve criar o objeto de autenticação com sucesso e mapear as roles")
    void shouldCreateAuthenticationWithAuthorities() {
        // Arrange
        String mockName = "João Silva";

        Role roleUser = mock(Role.class);
        when(roleUser.getAuthority()).thenReturn("ROLE_USER");

        Role roleAdmin = mock(Role.class);
        when(roleAdmin.getAuthority()).thenReturn("ROLE_ADMIN");

        Set<Role> mockRoles = Set.of(roleUser, roleAdmin);

        when(userIdentification.getName()).thenReturn(mockName);
        when(userIdentification.getRoles()).thenReturn(mockRoles); // Agora os tipos coincidem

        // Act
        CustomAuthentication auth = new CustomAuthentication(userIdentification);

        // Assert
        assertEquals(mockName, auth.getName());
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Deve lançar ExceptionInInitializerError quando o usuário for nulo")
    void shouldThrowExceptionWhenUserIsNull() {
        assertThrows(ExceptionInInitializerError.class, () -> {
            new CustomAuthentication(null);
        });
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar alterar o estado de autenticação")
    void shouldThrowExceptionWhenSetAuthenticatedIsCalled() {
        CustomAuthentication auth = new CustomAuthentication(userIdentification);

        assertThrows(IllegalArgumentException.class, () -> {
            auth.setAuthenticated(false);
        });
    }
}