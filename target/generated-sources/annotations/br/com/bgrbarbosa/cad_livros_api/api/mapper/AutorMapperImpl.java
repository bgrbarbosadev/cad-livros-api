package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.AutorDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T09:15:25-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class AutorMapperImpl implements AutorMapper {

    @Override
    public Autor parseToEntity(AutorDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Autor autor = new Autor();

        autor.setIdAutor( dto.idAutor() );
        autor.setNameAutor( dto.nameAutor() );
        autor.setBiografiaAutor( dto.biografiaAutor() );
        autor.setDtNascAutor( dto.dtNascAutor() );
        autor.setNacionalidadeAutor( dto.nacionalidadeAutor() );
        autor.setFotoAutor( dto.fotoAutor() );
        List<Livro> list = dto.livros();
        if ( list != null ) {
            autor.setLivros( new ArrayList<Livro>( list ) );
        }

        return autor;
    }

    @Override
    public AutorDTO parseToDto(Autor entity) {
        if ( entity == null ) {
            return null;
        }

        UUID idAutor = null;
        String nameAutor = null;
        String biografiaAutor = null;
        LocalDate dtNascAutor = null;
        String nacionalidadeAutor = null;
        String fotoAutor = null;
        List<Livro> livros = null;

        idAutor = entity.getIdAutor();
        nameAutor = entity.getNameAutor();
        biografiaAutor = entity.getBiografiaAutor();
        dtNascAutor = entity.getDtNascAutor();
        nacionalidadeAutor = entity.getNacionalidadeAutor();
        fotoAutor = entity.getFotoAutor();
        List<Livro> list = entity.getLivros();
        if ( list != null ) {
            livros = new ArrayList<Livro>( list );
        }

        AutorDTO autorDTO = new AutorDTO( idAutor, nameAutor, biografiaAutor, dtNascAutor, nacionalidadeAutor, fotoAutor, livros );

        return autorDTO;
    }

    @Override
    public List<AutorDTO> parseToListDTO(List<Autor> list) {
        if ( list == null ) {
            return null;
        }

        List<AutorDTO> list1 = new ArrayList<AutorDTO>( list.size() );
        for ( Autor autor : list ) {
            list1.add( parseToDto( autor ) );
        }

        return list1;
    }
}
