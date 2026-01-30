package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.EditoraDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
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
public class EditoraMapperImpl implements EditoraMapper {

    @Override
    public Editora parseToEntity(EditoraDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Editora editora = new Editora();

        editora.setIdEditora( dto.idEditora() );
        editora.setCnpjEditora( dto.cnpjEditora() );
        editora.setRazaoSocialEditora( dto.razaoSocialEditora() );
        editora.setTelEditora( dto.telEditora() );
        List<Livro> list = dto.livros();
        if ( list != null ) {
            editora.setLivros( new ArrayList<Livro>( list ) );
        }

        return editora;
    }

    @Override
    public EditoraDTO parseToDto(Editora entity) {
        if ( entity == null ) {
            return null;
        }

        UUID idEditora = null;
        String cnpjEditora = null;
        String razaoSocialEditora = null;
        String telEditora = null;
        List<Livro> livros = null;

        idEditora = entity.getIdEditora();
        cnpjEditora = entity.getCnpjEditora();
        razaoSocialEditora = entity.getRazaoSocialEditora();
        telEditora = entity.getTelEditora();
        List<Livro> list = entity.getLivros();
        if ( list != null ) {
            livros = new ArrayList<Livro>( list );
        }

        EditoraDTO editoraDTO = new EditoraDTO( idEditora, cnpjEditora, razaoSocialEditora, telEditora, livros );

        return editoraDTO;
    }

    @Override
    public List<EditoraDTO> parseToListDTO(List<Editora> list) {
        if ( list == null ) {
            return null;
        }

        List<EditoraDTO> list1 = new ArrayList<EditoraDTO>( list.size() );
        for ( Editora editora : list ) {
            list1.add( parseToDto( editora ) );
        }

        return list1;
    }
}
