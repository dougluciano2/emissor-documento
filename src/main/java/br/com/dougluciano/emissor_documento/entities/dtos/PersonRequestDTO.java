package br.com.dougluciano.emissor_documento.entities.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PersonRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotBlank(message = "CPF é obrigatóirio")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 digitos")
    String cpf,

    @NotBlank(message = "Endereço é obrigatório")
    String address,

    String medicalDiagnostics
) {}
