package br.com.bgrbarbosa.cad_livros_api.infraestruture.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserIdentification implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String login;
    private Set<Role> roles;

    public UserIdentification(UUID id, String name, String login, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        if(roles == null){
            roles = new HashSet<>();
        }
        return roles;
    }
}
