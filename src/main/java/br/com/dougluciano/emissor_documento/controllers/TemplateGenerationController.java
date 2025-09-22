package br.com.dougluciano.emissor_documento.controllers;

import br.com.dougluciano.emissor_documento.entities.dtos.GenerateDocumentRequestDTO;
import br.com.dougluciano.emissor_documento.services.TemplateGenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generate-document")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TemplateGenerationController {

    private final TemplateGenerationService service;

    @PostMapping
    public ResponseEntity<String> generate(@RequestBody @Valid GenerateDocumentRequestDTO request){

        String generatedHtml = service.generateDocumentFromTemplate(
                request.templateId(),
                request.personId()
        );

        return ResponseEntity.ok(generatedHtml);
    }
}
