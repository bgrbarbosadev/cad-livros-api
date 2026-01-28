package br.com.bgrbarbosa.cad_livros_api.api.dto;


public record UserRequestDTO(
        String email,
        String password)
{ }
