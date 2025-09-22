package br.com.dougluciano.emissor_documento.entities.dtos;

import jakarta.validation.constraints.NotBlank;

public record TemplateRequestDTO (

        @NotBlank(message = "O nome do modelo é obrigatório")
        String name,

        @NotBlank(message = "O conteúdo do modelo é obrigatório")
        String htmlContent
){
}
