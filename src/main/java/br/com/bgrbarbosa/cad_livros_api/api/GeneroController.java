package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.api.dto.GeneroDTO;
import br.com.bgrbarbosa.cad_livros_api.api.mapper.GeneroMapper;
import br.com.bgrbarbosa.cad_livros_api.business.GeneroService;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Genero;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.exceptions.ResourceNotFoundException;
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
@RequestMapping(value = "/api/v1/genero")
@RequiredArgsConstructor
@Tag(name = "Genero", description = "Contém as operações para controle de cadastro de genero.")
public class GeneroController {

	private final GeneroService service;
	private final GeneroMapper mapper;

	@GetMapping
	@Operation(
			summary = "Listar todos os generos",
			description = "Listar todos os generos cadastrados",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista todos os generos cadastrados",
							content = @Content(mediaType = "application/json"))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Page<GeneroDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<GeneroDTO> listDTO = mapper.parseToListDTO(service.findAll());
		Page<GeneroDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Recuperar um genero pelo id", description = "Recuperar uma genero pelo id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Genero recuperado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneroDTO.class))),
					@ApiResponse(responseCode = "404", description = "Genero não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<GeneroDTO> findById(@PathVariable UUID id) {
		GeneroDTO dto = mapper.parseToDto(service.findById(id));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma novo genero", description = "Recurso para cadastrar generos",
			responses = {
					@ApiResponse(responseCode = "201", description = "Genero cadastrada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneroDTO.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<GeneroDTO> insert(@RequestBody @Valid GeneroDTO dto) {
		Genero result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getIdGenero()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	@Operation(summary = "Atualizar genero", description = "Atualizar registro de genero",
			responses = {
					@ApiResponse(responseCode = "204", description = "Genero atualizado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneroDTO.class))),
					@ApiResponse(responseCode = "404", description = "Genero não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<GeneroDTO> update(@RequestBody @Valid GeneroDTO dto) {
		Genero result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleção de genero", description = "Deletar uma genero pelo ID",
			responses = {
					@ApiResponse(responseCode = "202", description = "Genero deletadao com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneroDTO.class))),
					@ApiResponse(responseCode = "404", description = "Genero não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}