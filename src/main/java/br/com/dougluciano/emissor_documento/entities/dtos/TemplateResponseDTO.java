package br.com.dougluciano.emissor_documento.entities.dtos;

import java.time.LocalDateTime;

public record TemplateResponseDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
