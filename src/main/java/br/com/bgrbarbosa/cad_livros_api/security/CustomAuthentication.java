package br.com.bgrbarbosa.cad_livros_api.security;

import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.UserIdentification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public class CustomAuthentication implements Authentication {

    private final UserIdentification userIdentification;

    public CustomAuthentication(UserIdentification userIdentification) {
        if(userIdentification == null){
            throw new ExceptionInInitializerError(Messages.USER_NOT_IDENTIFICATION);
        }
        this.userIdentification = userIdentification;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userIdentification
                .getRoles()
                .stream()
                .map(perm -> new SimpleGrantedAuthority(perm.getAuthority()))
                .toList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userIdentification;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException(Messages.USER_AUTHENTICATION);
    }

    @Override
    public String getName() {
        return this.userIdentification.getName();
    }
}
