package br.com.bgrbarbosa.cad_livros_api.api;

import br.com.bgrbarbosa.cad_livros_api.api.dto.LivroDTO;
import br.com.bgrbarbosa.cad_livros_api.api.mapper.LivroMapper;
import br.com.bgrbarbosa.cad_livros_api.business.LivroService;
import br.com.bgrbarbosa.cad_livros_api.infraestruture.domain.Livro;
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
@RequestMapping(value = "/api/v1/livro")
@RequiredArgsConstructor
@Tag(name = "Livro", description = "Contém as operações para controle de cadastro de livros.")
public class LivroController {

	private final LivroService service;
	private final LivroMapper mapper;

	@GetMapping
	@Operation(
			summary = "Listar todos os livros",
			description = "Listar todos os livros cadastrados",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista todos os livros cadastrados",
							content = @Content(mediaType = "application/json"))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Page<LivroDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<LivroDTO> listDTO = mapper.parseToListDTO(service.findAll());
		Page<LivroDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "Recuperar um livro pelo id", description = "Recuperar um livro pelo id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Livro recuperado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroDTO.class))),
					@ApiResponse(responseCode = "404", description = "Livro não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<LivroDTO> findById(@PathVariable UUID id) {
		LivroDTO dto = mapper.parseToDto(service.findById(id));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	@Operation(summary = "Cadastra uma novo livro", description = "Recurso para cadastrar livros",
			responses = {
					@ApiResponse(responseCode = "201", description = "Livro cadastrada com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroDTO.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<LivroDTO> insert(@RequestBody @Valid LivroDTO dto) {
		Livro result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getIdLivro()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	@Operation(summary = "Atualizar livro", description = "Atualizar registro de livro",
			responses = {
					@ApiResponse(responseCode = "204", description = "Livro atualizado com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroDTO.class))),
					@ApiResponse(responseCode = "404", description = "Livro não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<LivroDTO> update(@RequestBody @Valid LivroDTO dto) {
		Livro result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deleção de livro", description = "Deletar uma livro pelo ID",
			responses = {
					@ApiResponse(responseCode = "202", description = "Livro deletadao com sucesso",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroDTO.class))),
					@ApiResponse(responseCode = "404", description = "Livro não encontrado",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class)))
			})
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}