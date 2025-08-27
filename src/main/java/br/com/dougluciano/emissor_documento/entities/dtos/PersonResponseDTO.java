package br.com.dougluciano.emissor_documento.entities.dtos;

import java.time.LocalDateTime;

public record PersonResponseDTO (
        Long id,
        String name,
        String cpf,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
)
{}
