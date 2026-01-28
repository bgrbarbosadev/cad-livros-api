package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.LivroRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Teste de LivroServiceImpl")
class LivroServiceImplTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroServiceImpl service;

    private Livro l1;
    private Livro l2;
    private Livro l3;
    private List<Livro> listaLivros;

    private UUID idExistente;
    private UUID idInexistente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.l1 = new Livro(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Livro Um",
                "ISBN-1111",
                java.time.LocalDate.of(2001, 1, 1),
                "1ª edição",
                new java.math.BigDecimal("59.90"),
                new Autor(UUID.fromString("11111111-1111-1111-1111-111111111111"), "José Silva", "Autor focado em literatura brasileira contemporânea.", LocalDate.of(1980, 5, 12), "Brasil", "jose_silva.jpg", List.<Livro>of()),
                new Genero(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Ficção Científica", null),
                new Editora(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "12.345.678/0001-90", "Editora Alpha", "(11) 1234-5678", new ArrayList<>())
        );

        this.l2 = new Livro(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Livro Dois",
                "ISBN-2222",
                java.time.LocalDate.of(2002, 1, 1),
                "1ª edição",
                new java.math.BigDecimal("69.90"),
                new Autor(UUID.fromString("11111111-1111-1111-1111-111111111111"), "José Silva", "Autor focado em literatura brasileira contemporânea.", LocalDate.of(1980, 5, 12), "Brasil", "jose_silva.jpg", List.<Livro>of()),
                new Genero(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Ficção Científica", null),
                new Editora(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "12.345.678/0001-90", "Editora Alpha", "(11) 1234-5678", new ArrayList<>())
        );

        this.l3 = new Livro(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Livro Três",
                "ISBN-3333",
                java.time.LocalDate.of(2003, 1, 1),
                "1ª edição",
                new java.math.BigDecimal("79.90"),
                new Autor(UUID.fromString("11111111-1111-1111-1111-111111111111"), "José Silva", "Autor focado em literatura brasileira contemporânea.", LocalDate.of(1980, 5, 12), "Brasil", "jose_silva.jpg", List.<Livro>of()),
                new Genero(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Ficção Científica", null),
                new Editora(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "12.345.678/0001-90", "Editora Alpha", "(11) 1234-5678", new ArrayList<>())
        );
        this.listaLivros = List.of(l1, l2, l3);
        this.idExistente = l1.getIdLivro();
        this.idInexistente = UUID.fromString("44444444-4444-4444-4444-444444444444");
    }

    @Nested
    @DisplayName("inserir")
    class InsertTests {
        @Test
        @DisplayName("Deve inserir e retornar o Livro salvo")
        void shouldInsertAndReturnSavedLivro() {
            when(repository.save(l1)).thenReturn(l1);

            Livro result = service.insert(l1);

            assertEquals(l1, result);
            verify(repository).save(l1);
        }
    }

    @Nested
    @DisplayName("buscarTodos")
    class FindAllTests {
        @Test
        @DisplayName("Deve retornar lista paginada de Livros")
        void shouldReturnPaginatedListOfLivros() {
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Livro> page = new PageImpl<>(listaLivros, pageRequest, listaLivros.size());
            when(repository.findAll(pageRequest)).thenReturn(page);

            Page<Livro> result = service.findAll(pageRequest);

            assertEquals(page, result);
            verify(repository).findAll(pageRequest);
        }

        @Test
        @DisplayName("Deve retornar todos os Livros")
        void shouldReturnAllLivros() {
            when(repository.findAll()).thenReturn(listaLivros);

            List<Livro> result = service.findAll();

            assertEquals(listaLivros, result);
            assertEquals(listaLivros.size(), result.size());
            assertEquals(listaLivros.get(0), result.get(0));
            verify(repository).findAll();
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class FindByIdTests {
        @Test
        @DisplayName("Deve retornar Livro quando encontrado por ID")
        void shouldReturnLivroWhenFoundById() {
            when(repository.findById(l1.getIdLivro())).thenReturn(Optional.of(l1));

            Livro result = service.findById(idExistente);

            assertEquals(l1, result);
            assertEquals(l1.toString(), result.toString());
            verify(repository).findById(idExistente);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando Livro não for encontrado por ID")
        void shouldThrowExceptionWhenLivroNotFoundById() {
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
        @DisplayName("Deve deletar Livro quando o ID existir")
        void shouldDeleteLivroWhenIdExists() {
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
        @DisplayName("Deve atualizar e retornar o Livro atualizado")
        void shouldUpdateAndReturnUpdatedLivro() {
            l1.setTituloLivro("Titulo alterado");
            when(repository.existsById(l1.getIdLivro())).thenReturn(true);
            when(repository.save(l1)).thenReturn(l1);

            Livro result = service.update(l1);

            assertEquals(l1, result);
            assertEquals("Titulo alterado", result.getTituloLivro());
            verify(repository).existsById(l1.getIdLivro());
            verify(repository).save(l1);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando o Livro não existir")
        void shouldThrowExceptionWhenLivroDoesNotExist() {
            Livro livro = new Livro();
            livro.setIdLivro(UUID.randomUUID());
            when(repository.existsById(livro.getIdLivro())).thenReturn(false);

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.update(livro));

            assertEquals(Messages.RESOURCE_NOT_FOUND, exception.getMessage());
            verify(repository).existsById(livro.getIdLivro());
            verify(repository, never()).save(livro);
        }
    }
}