package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.GeneroRepository;
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

class GeneroServiceImplTest {

    @Mock
    private GeneroRepository repository;

    @InjectMocks
    private GeneroServiceImpl service;

    private Genero g1;
    private Genero g2;
    private Genero g3;
    private List<Genero> generos;

    private UUID idExistente;
    private UUID idInexistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.g1 = new Genero(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                "Ficção Científica",
                new ArrayList<>()
        );

        this.g2 = new Genero(
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "Romance",
                new ArrayList<>()
        );

        this.g3 = new Genero(
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "Mistério",
                new ArrayList<>()
        );

        this.generos = List.of(g1, g2, g3);
        this.idExistente = g1.getIdGenero();
        this.idInexistente = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
    }

    @Nested
    @DisplayName("insert")
    class InsertTests {
        @Test
        @DisplayName("Deve inserir e retornar o Genero salvo")
        void shouldInsertAndReturnSavedGenero() {
            when(repository.save(g1)).thenReturn(g1);

            Genero result = service.insert(g1);

            assertEquals(g1, result);
            assertEquals(g1.getIdGenero(), result.getIdGenero());
            assertEquals(g1.getDescGenero(), result.getDescGenero());
            verify(repository).save(g1);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAllTests {
        @Test
        @DisplayName("Deve retornar lista paginada de Generos")
        void shouldReturnPaginatedListOfGeneros() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Genero> page = new PageImpl<>(generos);
            when(repository.findAll(pageable)).thenReturn(page);

            Page<Genero> result = service.findAll(pageable);

            assertEquals(page, result);
            assertEquals(page.getTotalElements(), result.getTotalElements());
            assertEquals(page.getContent().get(0), result.getContent().get(0));
            verify(repository).findAll(pageable);
        }

        @Test
        @DisplayName("Deve retornar todas os Generos")
        void shouldReturnAllGeneros() {
            when(repository.findAll()).thenReturn(generos);

            List<Genero> result = service.findAll();

            assertEquals(generos, result);
            assertEquals(generos.size(), result.size());
            assertEquals(generos.get(0), result.get(0));
            verify(repository).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {
        @Test
        @DisplayName("Deve retornar Genero quando encontrado por ID")
        void shouldReturnGeneroWhenFoundById() {
            when(repository.findById(idExistente)).thenReturn(Optional.of(g1));

            Genero result = service.findById(idExistente);

            assertEquals(g1, result);
            assertEquals(g1.getIdGenero(), result.getIdGenero());
            assertEquals(g1.getDescGenero(), result.getDescGenero());
            assertEquals(g1.toString(), result.toString());
            verify(repository).findById(idExistente);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando Genero não encontrado por ID")
        void shouldThrowExceptionWhenGeneroNotFoundById() {
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
        @DisplayName("Deve deletar Genero quando ID existe")
        void shouldDeleteGeneroWhenIdExists() {
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
        @DisplayName("Deve atualizar e retornar o Genero atualizado")
        void shouldUpdateAndReturnUpdatedGenero() {
            g1.setDescGenero("Ficção  Atualizada");
            when(repository.existsById(g1.getIdGenero())).thenReturn(true);
            when(repository.save(g1)).thenReturn(g1);

            Genero result = service.update(g1);

            assertEquals(g1, result);
            verify(repository).existsById(g1.getIdGenero());
            verify(repository).save(g1);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando Genero não existe")
        void shouldThrowExceptionWhenGeneroDoesNotExist() {
            Genero genero = new Genero();
            genero.setIdGenero(UUID.randomUUID());
            when(repository.existsById(genero.getIdGenero())).thenReturn(false);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.update(genero));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).existsById(genero.getIdGenero());
            verify(repository, never()).save(genero);
        }
    }
}
