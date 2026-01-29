package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.api.dto.EditoraDTO;
import br.com.bgrbarbosa.cad_livros_api.api.mapper.EditoraMapper;
import br.com.bgrbarbosa.cad_livros_api.business.EditoraService;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Editora;
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
@RequestMapping(value = "/api/v1/editora")
@RequiredArgsConstructor
@Tag(name = "Editora", description = "Contém as operações para controle de cadastro de editoras.")
public class EditoraController {

	private final EditoraService service;
	private final EditoraMapper mapper;

	@GetMapping
	@Operation(
			summary = "Listar todos os Editoras",
			description = "Listar todos os editoras cadastrados",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista todos os editoras cadastradas",
							content = @Content(mediaType = "application/json"))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Page<EditoraDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<EditoraDTO> listDTO = mapper.parseToListDTO(service.findAll());
		Page<EditoraDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Recuperar um editora pelo id", description = "Recuperar uma editora pelo id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Editora recuperada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraDTO.class))),
					@ApiResponse(responseCode = "404", description = "Editora não encontrada",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<EditoraDTO> findById(@PathVariable UUID id) {
		EditoraDTO dto = mapper.parseToDto(service.findById(id));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma nova editora", description = "Recurso para cadastrar editoras",
			responses = {
					@ApiResponse(responseCode = "201", description = "Editora cadastrada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraDTO.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<EditoraDTO> insert(@RequestBody @Valid EditoraDTO dto) {
		Editora result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getIdEditora()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	@Operation(summary = "Atualizar editora", description = "Atualizar registro de editora",
			responses = {
					@ApiResponse(responseCode = "204", description = "Editora atualizada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraDTO.class))),
					@ApiResponse(responseCode = "404", description = "Editora não encontrada",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<EditoraDTO> update(@RequestBody @Valid EditoraDTO dto) {
		Editora result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleção de editora", description = "Deletar uma editora pelo ID",
			responses = {
					@ApiResponse(responseCode = "202", description = "Editora deletada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraDTO.class))),
					@ApiResponse(responseCode = "404", description = "Editora não encontrada",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}