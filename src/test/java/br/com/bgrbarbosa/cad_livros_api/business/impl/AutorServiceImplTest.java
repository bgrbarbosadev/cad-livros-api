package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.AutorRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorServiceImplTest {

    @Mock
    private AutorRepository repository;

    @InjectMocks
    private AutorServiceImpl service;

    private Autor a1;
    private Autor a2;
    private Autor a3;
    private List<Autor> autores;
    private UUID idExistente;
    private UUID idInexistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.a1 = new Autor(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "José Silva",
                "Autor focado em literatura brasileira contemporânea.",
                LocalDate.of(1980, 5, 12),
                "Brasil",
                "jose_silva.jpg",
                List.<Livro>of()
        );

        this.a2 = new Autor(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Maria Oliveira",
                "Especialista em romances históricos e biografias.",
                LocalDate.of(1975, 8, 23),
                "Portugal",
                "maria_oliveira.jpg",
                List.<Livro>of()
        );

        this.a3 = new Autor(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Carlos Pereira",
                "Autor de ficção científica e fantasia épica.",
                LocalDate.of(1990, 3, 15),
                "Angola",
                "carlos_pereira.jpg",
                List.<Livro>of()
        );

        this.autores = List.of(a1, a2, a3);
        this.idExistente = a1.getIdAutor();
        this.idInexistente = UUID.fromString("99999999-9999-9999-9999-999999999999");
    }

    @Nested
    @DisplayName("inserir")
    class InsertTests {
        @Test
        @DisplayName("Deve inserir e retornar o Autor salvo")
        void shouldInsertAndReturnSavedAutor() {
            Autor autor = new Autor();
            when(repository.save(autor)).thenReturn(autor);

            Autor result = service.insert(autor);

            assertEquals(autor, result);
            assertEquals(autor.toString(), result.toString());
            verify(repository).save(autor);
        }
    }

    @Nested
    @DisplayName("buscarTodos")
    class FindAllTests {
        @Test
        @DisplayName("Deve retornar lista paginada de Autores")
        void shouldReturnPaginatedListOfAutores() {
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Autor> page = new PageImpl<>(autores, pageRequest, autores.size());
            when(repository.findAll(pageRequest)).thenReturn(page);

            Page<Autor> result = service.findAll(pageRequest);

            assertEquals(page, result);
            verify(repository).findAll(pageRequest);
        }

        @Test
        @DisplayName("Deve retornar todos os Autores")
        void shouldReturnAllAutores() {
            when(repository.findAll()).thenReturn(autores);

            List<Autor> result = service.findAll();

            assertEquals(autores, result);
            assertEquals(autores.size(), result.size());
            assertEquals(autores.get(0), result.get(0));
            verify(repository).findAll();
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class FindByIdTests {
        @Test
        @DisplayName("Deve retornar Autor quando encontrado por ID")
        void shouldReturnAutorWhenFoundById() {
            when(repository.findById(a1.getIdAutor())).thenReturn(Optional.of(a1));

            Autor result = service.findById(idExistente);

            assertEquals(a1, result);
            assertEquals(a1.toString(), result.toString());
            verify(repository).findById(idExistente);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando Autor não for encontrado por ID")
        void shouldThrowExceptionWhenAutorNotFoundById() {
            when(repository.findById(idInexistente)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(idInexistente));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).findById(idInexistente);
        }
    }

    @Nested
    @DisplayName("deletar")
    class DeleteTests {
        @Test
        @DisplayName("Deve deletar Autor quando o ID existir")
        void shouldDeleteAutorWhenIdExists() {
            when(repository.existsById(idExistente)).thenReturn(true);

            service.delete(idExistente);

            verify(repository).existsById(idExistente);
            verify(repository).deleteById(idExistente);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o ID não existir")
        void shouldThrowExceptionWhenIdDoesNotExist() {
            when(repository.existsById(idInexistente)).thenReturn(false);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(idInexistente));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).existsById(idInexistente);
            verify(repository, never()).deleteById(idInexistente);
        }
    }

    @Nested
    @DisplayName("atualizar")
    class UpdateTests {
        @Test
        @DisplayName("Deve atualizar e retornar o Autor atualizado")
        void shouldUpdateAndReturnUpdatedAutor() {
            a1.setNameAutor("Autor alterado");
            when(repository.existsById(a1.getIdAutor())).thenReturn(true);
            when(repository.save(a1)).thenReturn(a1);

            Autor result = service.update(a1);

            assertEquals(a1, result);
            assertEquals("Autor alterado", result.getNameAutor());
            assertEquals(a1.toString(), result.toString());
            verify(repository).existsById(a1.getIdAutor());
            verify(repository).save(a1);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o Autor não existir")
        void shouldThrowExceptionWhenAutorDoesNotExist() {
            Autor autor = new Autor();
            autor.setIdAutor(UUID.randomUUID());
            when(repository.existsById(autor.getIdAutor())).thenReturn(false);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.update(autor));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).existsById(autor.getIdAutor());
            verify(repository, never()).save(autor);
        }
    }
}