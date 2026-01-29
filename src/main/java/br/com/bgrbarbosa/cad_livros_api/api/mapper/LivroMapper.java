package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.LivroDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro parseToEntity(LivroDTO dto);

    LivroDTO parseToDto(Livro entity);

    List<LivroDTO> parseToListDTO(List<Livro>list);

    default Page<LivroDTO> toPageDTO(List<LivroDTO> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<LivroDTO> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}
