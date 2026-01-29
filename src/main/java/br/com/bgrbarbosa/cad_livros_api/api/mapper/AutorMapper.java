package br.com.bgrbarbosa.cad_livros_api.api.mapper;


import br.com.bgrbarbosa.cad_livros_api.api.dto.AutorDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor parseToEntity(AutorDTO dto);

    AutorDTO parseToDto(Autor entity);

    List<AutorDTO> parseToListDTO(List<Autor>list);

    default Page<AutorDTO> toPageDTO(List<AutorDTO> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<AutorDTO> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}
