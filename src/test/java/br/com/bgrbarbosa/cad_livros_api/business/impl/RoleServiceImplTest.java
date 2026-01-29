package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.RoleException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleServiceImpl service;

    private Role role;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        role = new Role();
        role.setUuid(id);
        role.setAuthority("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Deve inserir uma role com sucesso")
    void insert_ShouldReturnRole_WhenSuccessful() throws RoleException {
        when(repository.existsByAuthority(role.getAuthority())).thenReturn(false);
        when(repository.save(any(Role.class))).thenReturn(role);

        Role savedRole = service.insert(role);

        assertNotNull(savedRole);
        assertEquals(role.getAuthority(), savedRole.getAuthority());
        verify(repository, times(1)).save(role);
    }

    @Test
    @DisplayName("Deve lançar RoleException ao tentar inserir role já existente")
    void insert_ShouldThrowRoleException_WhenRoleExists() {
        when(repository.existsByAuthority(role.getAuthority())).thenReturn(true);

        assertThrows(RoleException.class, () -> service.insert(role));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar role por ID com sucesso")
    void findById_ShouldReturnRole_WhenIdExists() {
        when(repository.findById(id)).thenReturn(Optional.of(role));

        Role foundRole = service.findById(id);

        assertNotNull(foundRole);
        assertEquals(id, foundRole.getUuid());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }

    @Test
    @DisplayName("Deve deletar role com sucesso")
    void delete_ShouldDelete_WhenIdExists() {
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        assertDoesNotThrow(() -> service.delete(id));
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deve atualizar role com sucesso")
    void update_ShouldReturnUpdatedRole_WhenIdExists() {
        Role updatedData = new Role();
        updatedData.setUuid(id);
        updatedData.setAuthority("ROLE_USER");

        when(repository.findById(id)).thenReturn(Optional.of(role));
        when(repository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Role result = service.update(updatedData);

        assertEquals("ROLE_USER", result.getAuthority());
        verify(repository).save(any(Role.class));
    }

    @Test
    @DisplayName("Deve retornar lista de roles")
    void findAll_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(role));

        List<Role> result = service.findAll(Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}