package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.api.dto.UserDTO;
import br.com.bgrbarbosa.cad_livros_api.api.mapper.UserMapper;
import br.com.bgrbarbosa.cad_livros_api.business.UserService;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.User;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.UserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Contém as operações para controle de cadastro de usuários.")
public class UserController {

	private final UserService service;
	private final UserMapper mapper;

	@GetMapping
	@Operation(
			summary = "Listar todos os usuários",
			description = "Listar todos os usuários cadastrados",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista todos os usuários cadastrados",
							content = @Content(mediaType = "application/json"))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Page<UserDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<UserDTO> listDTO = mapper.parseToListDTO(service.findAll(page));
		Page<UserDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Recuperar um usuario pelo id", description = "Recuperar um usuario pelo id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Usuario recuperado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = "Usuario não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
		UserDTO dto = mapper.parseToDto(service.findById(id));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma novo usuario", description = "Recurso para cadastrar usuarios",
			responses = {
					@ApiResponse(responseCode = "201", description = "Usuario cadastrada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserDTO dto) throws UserException {
		User result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getUuid()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	@Operation(summary = "Atualizar usuario", description = "Atualizar registro de usuario",
			responses = {
					@ApiResponse(responseCode = "204", description = "Usuario atualizado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = "Usuario não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO dto) {
		User result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleção de usuario", description = "Deletar um usuario pelo ID",
			responses = {
					@ApiResponse(responseCode = "202", description = "Usuario deletadao com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
					@ApiResponse(responseCode = "404", description = "Usuario não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}