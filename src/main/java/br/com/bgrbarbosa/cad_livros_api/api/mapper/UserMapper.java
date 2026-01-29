package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.UserDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User parseToEntity(UserDTO dto);

    UserDTO parseToDto(User entity);

    List<UserDTO> parseToListDTO(List<User>list);

    default Page<UserDTO> toPageDTO(List<UserDTO> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<UserDTO> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}
