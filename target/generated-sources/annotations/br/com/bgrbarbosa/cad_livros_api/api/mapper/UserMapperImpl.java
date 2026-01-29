package br.com.bgrbarbosa.cad_livros_api.api.mapper;

import br.com.bgrbarbosa.cad_livros_api.api.dto.UserDTO;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Role;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-28T14:29:37-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User parseToEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setUuid( dto.uuid() );
        user.setFirstName( dto.firstName() );
        user.setLastName( dto.lastName() );
        user.setEmail( dto.email() );
        user.setPassword( dto.password() );
        Set<Role> set = dto.roles();
        if ( set != null ) {
            user.setRoles( new LinkedHashSet<Role>( set ) );
        }

        return user;
    }

    @Override
    public UserDTO parseToDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UUID uuid = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String password = null;
        Set<Role> roles = null;

        uuid = entity.getUuid();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        password = entity.getPassword();
        Set<Role> set = entity.getRoles();
        if ( set != null ) {
            roles = new LinkedHashSet<Role>( set );
        }

        UserDTO userDTO = new UserDTO( uuid, firstName, lastName, email, password, roles );

        return userDTO;
    }

    @Override
    public List<UserDTO> parseToListDTO(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDTO> list1 = new ArrayList<UserDTO>( list.size() );
        for ( User user : list ) {
            list1.add( parseToDto( user ) );
        }

        return list1;
    }
}
