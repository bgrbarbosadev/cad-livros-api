package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.api.dto.AutorDTO;
import br.com.bgrbarbosa.cad_livros_api.api.mapper.AutorMapper;
import br.com.bgrbarbosa.cad_livros_api.business.AutorService;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Autor;
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
@RequestMapping(value = "/api/v1/autor")
@RequiredArgsConstructor
@Tag(name = "Autor", description = "Contém as operações para controle de cadastro de autor.")
public class AutorController {

	private final AutorService service;
	private final AutorMapper mapper;

	@GetMapping
	@Operation(
			summary = "Listar todos os Autores",
			description = "Listar todos os autores cadastrados",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista todos os autores cadastrados",
							content = @Content(mediaType = "application/json"))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Page<AutorDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<AutorDTO> listDTO = mapper.parseToListDTO(service.findAll());
		Page<AutorDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Recuperar um autor pelo id", description = "Recuperar um autor pelo id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Autor recuperado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class))),
					@ApiResponse(responseCode = "404", description = "Autor não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<AutorDTO> findById(@PathVariable UUID id) {
		AutorDTO dto = mapper.parseToDto(service.findById(id));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo autor", description = "Recurso para cadastrar autores",
			responses = {
					@ApiResponse(responseCode = "201", description = "Autor cadastrado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<AutorDTO> insert(@RequestBody @Valid AutorDTO dto) {
		Autor result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getIdAutor()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	@Operation(summary = "Atualizar autor", description = "Atualizar registro de autor",
			responses = {
					@ApiResponse(responseCode = "204", description = "Autor atualizado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class))),
					@ApiResponse(responseCode = "404", description = "Autor não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<AutorDTO> update(@RequestBody @Valid AutorDTO dto) {
		Autor result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleção de autor", description = "Deletar um autor pelo ID",
			responses = {
					@ApiResponse(responseCode = "202", description = "Autor deletado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorDTO.class))),
					@ApiResponse(responseCode = "404", description = "Cliente não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}