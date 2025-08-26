package br.com.dougluciano.emissor_documento.entities.dtos;

public record PersonResponseDTO (
        Long id,
        String name,
        String cpf,
        String address
)
{}
