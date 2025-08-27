package br.com.dougluciano.emissor_documento.entities.dtos;


import java.time.LocalDateTime;

public record DocumentResponseDTO(
        Long id,
        String title,
        String fileType,
        String storagePath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}