package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.api.dto.UserRequestDTO;
import br.com.bgrbarbosa.cad_livros_api.api.dto.UserResponseDTO;
import br.com.bgrbarbosa.cad_livros_api.business.UserService;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.UserIdentification;
import br.com.bgrbarbosa.cad_livros_api.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Controle de login de usuários")
public class LoginController {

    private final UserService service;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO dto) {

        // Cria um token de autenticação com o email e senha fornecidos
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        // 2. O AuthenticationManager chama o seu CustomAuthenticationProvider internamente e retorna os dados do usuário autenticado
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Pagamos os dado do usuário autenticado no objeto principal do Authentication para gerar o token JWT
        var user = (UserIdentification) auth.getPrincipal();

        var tokenResponse = tokenService.generateToken(new User(
                user.getId(),
                user.getName(),
                null,
                user.getLogin(),
                dto.password(),
                user.getRoles()
        ));

        return ResponseEntity.ok(tokenResponse);

    }
}
