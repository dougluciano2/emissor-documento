package br.com.dougluciano.emissor_documento.entities.dtos;

import jakarta.validation.constraints.NotNull;

public record GenerateDocumentRequestDTO (
        @NotNull Long templateId,
        @NotNull Long personId
){
}
