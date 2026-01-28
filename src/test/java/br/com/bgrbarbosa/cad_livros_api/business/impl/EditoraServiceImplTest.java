package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.EditoraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditoraServiceImplTest {

    @Mock
    private EditoraRepository repository;

    @InjectMocks
    private EditoraServiceImpl service;

    private Editora e1;
    private Editora e2;
    private Editora e3;

    private List<Editora> editoras;

    private UUID idExistente;
    private UUID idInexistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.e1 = new Editora(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                "12.345.678/0001-90",
                "Editora Alpha",
                "(11) 1234-5678",
                new ArrayList<>()
        );

        this.e2 = new Editora(
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "98.765.432/0001-09",
                "Editora Beta",
                "(21) 8765-4321",
                new ArrayList<>()
        );

        this.e3 = new Editora(
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "11.222.333/0001-44",
                "Editora Gamma",
                "(31) 1122-3344",
                new ArrayList<>()
        );

        this.idExistente = e1.getIdEditora();
        this.idInexistente = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
        this.editoras = List.of(e1, e2, e3);
    }

    @Nested
    @DisplayName("insert")
    class InsertTests {
        @Test
        @DisplayName("Deve inserir e retornar a Editora salva")
        void shouldInsertAndReturnSavedEditora() {
            when(repository.save(e1)).thenReturn(e1);

            Editora result = service.insert(e1);

            assertEquals(e1, result);
            verify(repository).save(e1);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAllTests {
        @Test
        @DisplayName("Deve retornar lista paginada de Editoras")
        void shouldReturnPaginatedListOfEditoras() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Editora> page = new PageImpl<>(editoras, pageable, editoras.size());
            when(repository.findAll(pageable)).thenReturn(page);

            Page<Editora> result = service.findAll(pageable);

            assertEquals(page, result);
            assertEquals(page.getTotalElements(), result.getTotalElements());
            assertEquals(page.getContent().get(0), result.getContent().get(0));
            verify(repository).findAll(pageable);
        }

        @Test
        @DisplayName("Deve retornar todas as Editoras")
        void shouldReturnAllEditoras() {
            when(repository.findAll()).thenReturn(editoras);

            List<Editora> result = service.findAll();

            assertEquals(editoras, result);
            assertEquals(editoras.size(), result.size());
            assertEquals(editoras.get(0), result.get(0));
            verify(repository).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {
        @Test
        @DisplayName("Deve retornar Editora quando encontrada por ID")
        void shouldReturnEditoraWhenFoundById() {
            when(repository.findById(idExistente)).thenReturn(Optional.of(e1));

            Editora result = service.findById(idExistente);

            assertEquals(e1, result);
            assertEquals(e1.getIdEditora(), result.getIdEditora());
            assertEquals(e1.getRazaoSocialEditora(), result.getRazaoSocialEditora());
            assertEquals(e1.toString(), result.toString());
            verify(repository).findById(idExistente);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando Editora não encontrada por ID")
        void shouldThrowExceptionWhenEditoraNotFoundById() {
            when(repository.findById(idInexistente)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(idInexistente));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).findById(idInexistente);
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTests {
        @Test
        @DisplayName("Deve deletar Editora quando ID existe")
        void shouldDeleteEditoraWhenIdExists() {
            when(repository.existsById(idExistente)).thenReturn(true);

            service.delete(idExistente);

            verify(repository).existsById(idExistente);
            verify(repository).deleteById(idExistente);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando ID não existe")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            when(repository.existsById(idInexistente)).thenReturn(false);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(idInexistente));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).existsById(idInexistente);
            verify(repository, never()).deleteById(idInexistente);
        }
    }

    @Nested
    @DisplayName("update")
    class UpdateTests {
        @Test
        @DisplayName("Deve atualizar e retornar a Editora atualizada")
        void shouldUpdateAndReturnUpdatedEditora() {
            e1.setRazaoSocialEditora("Editora Alpha Updated");
            when(repository.existsById(e1.getIdEditora())).thenReturn(true);
            when(repository.save(e1)).thenReturn(e1);

            Editora result = service.update(e1);

            assertEquals("Editora Alpha Updated", result.getRazaoSocialEditora());
            verify(repository).existsById(e1.getIdEditora());
            verify(repository).save(e1);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando Editora não existe")
        void shouldThrowExceptionWhenEditoraDoesNotExist() {
            Editora editora = new Editora();
            editora.setIdEditora(UUID.randomUUID());
            when(repository.existsById(editora.getIdEditora())).thenReturn(false);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.update(editora));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).existsById(editora.getIdEditora());
            verify(repository, never()).save(editora);
        }
    }
}