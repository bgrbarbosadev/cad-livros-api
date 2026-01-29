package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.UserException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private UUID id;
    private final String rawPassword = "password123";
    private final String encodedPassword = "encoded_password";

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        user = new User();
        user.setUuid(id);
        user.setEmail("test@example.com");
        user.setPassword(rawPassword);
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    @DisplayName("Deve carregar usuário pelo email com sucesso")
    void loadUserByUsername_ShouldReturnUser_WhenEmailExists() {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = service.loadUserByUsername(user.getEmail());

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando email não existir")
    void loadUserByUsername_ShouldThrowException_WhenEmailDoesNotExist() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Deve inserir usuário com senha criptografada")
    void insert_ShouldEncryptPasswordAndSave_WhenSuccessful() throws UserException {
        when(repository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(repository.save(any(User.class))).thenReturn(user);

        User savedUser = service.insert(user);

        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        verify(passwordEncoder).encode(rawPassword);
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Deve lançar UserException ao inserir email duplicado")
    void insert_ShouldThrowException_WhenEmailExists() {
        when(repository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(UserException.class, () -> service.insert(user));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar por ID com sucesso")
    void findById_ShouldReturnUser_WhenIdExists() {
        when(repository.findById(id)).thenReturn(Optional.of(user));

        User result = service.findById(id);

        assertThat(result.getUuid()).isEqualTo(id);

    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void findById_ShouldThrowException_WhenIdNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }

    @Test
    @DisplayName("Deve deletar usuário quando ID existir")
    void delete_ShouldCallRepository_WhenIdExists() {
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void delete_ShouldThrowException_WhenIdNotFound() {
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
    }

    @Test
    @DisplayName("Deve atualizar todos os campos e criptografar nova senha")
    void update_ShouldUpdateAllFields_WhenUserExists() {
        User updatedData = new User();
        updatedData.setUuid(id);
        updatedData.setFirstName("Jane");
        updatedData.setLastName("Smith");
        updatedData.setEmail("jane@example.com");
        updatedData.setPassword("newPassword");

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("new_encoded_password");
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = service.update(updatedData);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getPassword()).isEqualTo("new_encoded_password");
        verify(repository).save(any(User.class));
    }
}