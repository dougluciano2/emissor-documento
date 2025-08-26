package br.com.dougluciano.emissor_documento.entities.dtos;

import jakarta.validation.constraints.NotBlank;

public record DocumentUploadRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "O conteúdo HTML é obrigatório")
        String htmlContent
) {
}
