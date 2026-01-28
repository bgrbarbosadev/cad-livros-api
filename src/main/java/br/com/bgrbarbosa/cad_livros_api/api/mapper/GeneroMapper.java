package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.GeneroDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeneroMapper {

    Genero parseToEntity(GeneroDTO dto);

    GeneroDTO parseToDto(Genero entity);

    List<GeneroDTO> parseToListDTO(List<Genero>list);

    default Page<GeneroDTO> toPageDTO(List<GeneroDTO> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<GeneroDTO> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}
