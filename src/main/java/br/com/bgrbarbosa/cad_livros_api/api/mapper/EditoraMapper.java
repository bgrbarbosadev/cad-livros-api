package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.EditoraDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EditoraMapper {

    Editora parseToEntity(EditoraDTO dto);

    EditoraDTO parseToDto(Editora entity);

    List<EditoraDTO> parseToListDTO(List<Editora>list);

    default Page<EditoraDTO> toPageDTO(List<EditoraDTO> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<EditoraDTO> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}
