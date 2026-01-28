package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.LivroDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-28T14:29:37-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class LivroMapperImpl implements LivroMapper {

    @Override
    public Livro parseToEntity(LivroDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Livro livro = new Livro();

        livro.setIdLivro( dto.idLivro() );
        livro.setTituloLivro( dto.tituloLivro() );
        livro.setIsbn( dto.isbn() );
        livro.setAnoPublicacao( dto.anoPublicacao() );
        livro.setEdicaoLivro( dto.edicaoLivro() );
        livro.setPrecoLivro( dto.precoLivro() );
        livro.setAutorLivro( dto.autorLivro() );
        livro.setGeneroLivro( dto.generoLivro() );
        livro.setEditoraLivro( dto.editoraLivro() );

        return livro;
    }

    @Override
    public LivroDTO parseToDto(Livro entity) {
        if ( entity == null ) {
            return null;
        }

        UUID idLivro = null;
        String tituloLivro = null;
        String isbn = null;
        LocalDate anoPublicacao = null;
        String edicaoLivro = null;
        BigDecimal precoLivro = null;
        Autor autorLivro = null;
        Genero generoLivro = null;
        Editora editoraLivro = null;

        idLivro = entity.getIdLivro();
        tituloLivro = entity.getTituloLivro();
        isbn = entity.getIsbn();
        anoPublicacao = entity.getAnoPublicacao();
        edicaoLivro = entity.getEdicaoLivro();
        precoLivro = entity.getPrecoLivro();
        autorLivro = entity.getAutorLivro();
        generoLivro = entity.getGeneroLivro();
        editoraLivro = entity.getEditoraLivro();

        LivroDTO livroDTO = new LivroDTO( idLivro, tituloLivro, isbn, anoPublicacao, edicaoLivro, precoLivro, autorLivro, generoLivro, editoraLivro );

        return livroDTO;
    }

    @Override
    public List<LivroDTO> parseToListDTO(List<Livro> list) {
        if ( list == null ) {
            return null;
        }

        List<LivroDTO> list1 = new ArrayList<LivroDTO>( list.size() );
        for ( Livro livro : list ) {
            list1.add( parseToDto( livro ) );
        }

        return list1;
    }
}
