package br.com.dougluciano.emissor_documento.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentUploadRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "O conteúdo HTML é obrigatório")
        String htmlContent,

        @NotNull(message = "O ID da pessoa é obrigatório")
        Long personId
) {
}
