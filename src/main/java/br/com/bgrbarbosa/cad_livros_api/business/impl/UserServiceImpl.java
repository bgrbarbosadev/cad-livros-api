package br.com.bgrbarbosa.cad_livros_api.business.impl;

import br.com.bgrbarbosa.cad_livros_api.business.UserService;
import br.com.bgrbarbosa.cad_livros_api.config.Messages;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.UserException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public User insert(User user) throws UserException {
        User aux = user;
        if (repository.existsByEmail(aux.getEmail())) {
            throw new UserException(Messages.Existing_User);
        }
        aux.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public List<User> findAll(Pageable page) {
        return repository.findAll();
    }

    @Override
    public User findById(UUID uuid) {
        return repository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND + uuid)
        );
    }

    @Override
    public void delete(UUID uuid) {
        if (!repository.existsById(uuid)) {
            throw new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND + uuid);
        }
        repository.deleteById(uuid);
    }

    @Override
    public User update(User user) {
        User aux = repository.findById(user.getUuid()).orElseThrow(
                () -> new ResourceNotFoundException(Messages.RESOURCE_NOT_FOUND));
        aux.setFirstName(user.getFirstName());
        aux.setLastName(user.getLastName());
        aux.setEmail(user.getEmail());
        aux.setPassword(passwordEncoder.encode(user.getPassword()));
        aux.setRoles(user.getRoles());
        return repository.save(aux);
    }

}
