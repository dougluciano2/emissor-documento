package br.com.dougluciano.emissor_documento.entities.dtos;


public record DocumentResponseDTO(
        Long id,
        String title,
        String fileType,
        String storagePath
) {
}