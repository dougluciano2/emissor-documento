package br.com.dougluciano.emissor_documento.controllers;

import br.com.dougluciano.emissor_documento.entities.dtos.TemplateRequestDTO;
import br.com.dougluciano.emissor_documento.entities.dtos.TemplateResponseDTO;
import br.com.dougluciano.emissor_documento.entities.mapper.TemplateMapper;
import br.com.dougluciano.emissor_documento.services.TemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TemplateController {

    private final TemplateService service;
    private final TemplateMapper mapper;


    @GetMapping
    public ResponseEntity<List<TemplateResponseDTO>> listAll() {
        var templates = service.findAll();
        var responseDTOs = templates.stream().map(mapper::toTemplateResponseDTO).toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<byte[]> getTemplateContent(@PathVariable Long id) {
        var content = service.getTemplateContent(id);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content.asByteArray());
    }

    @PostMapping
    public ResponseEntity<TemplateResponseDTO> create(@RequestBody @Valid TemplateRequestDTO requestDTO,
                                                      UriComponentsBuilder uriBuilder) {
        var savedTemplate = service.create(requestDTO);
        var responseDTO = mapper.toTemplateResponseDTO(savedTemplate);
        var uri = uriBuilder.path("/templates/{id}").buildAndExpand(responseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }
}
