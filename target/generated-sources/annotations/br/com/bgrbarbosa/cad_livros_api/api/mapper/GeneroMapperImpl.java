package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.GeneroDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
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
public class GeneroMapperImpl implements GeneroMapper {

    @Override
    public Genero parseToEntity(GeneroDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Genero genero = new Genero();

        genero.setIdGenero( dto.idGenero() );
        genero.setDescGenero( dto.descGenero() );
        List<Livro> list = dto.livros();
        if ( list != null ) {
            genero.setLivros( new ArrayList<Livro>( list ) );
        }

        return genero;
    }

    @Override
    public GeneroDTO parseToDto(Genero entity) {
        if ( entity == null ) {
            return null;
        }

        UUID idGenero = null;
        String descGenero = null;
        List<Livro> livros = null;

        idGenero = entity.getIdGenero();
        descGenero = entity.getDescGenero();
        List<Livro> list = entity.getLivros();
        if ( list != null ) {
            livros = new ArrayList<Livro>( list );
        }

        GeneroDTO generoDTO = new GeneroDTO( idGenero, descGenero, livros );

        return generoDTO;
    }

    @Override
    public List<GeneroDTO> parseToListDTO(List<Genero> list) {
        if ( list == null ) {
            return null;
        }

        List<GeneroDTO> list1 = new ArrayList<GeneroDTO>( list.size() );
        for ( Genero genero : list ) {
            list1.add( parseToDto( genero ) );
        }

        return list1;
    }
}
