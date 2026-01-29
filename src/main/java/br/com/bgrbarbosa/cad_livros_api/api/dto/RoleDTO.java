package br.com.bgrbarbosa.cad_livros_api.api.dto;

import java.util.UUID;

public record RoleDTO(
        UUID uuid,
        String authority
) { }
