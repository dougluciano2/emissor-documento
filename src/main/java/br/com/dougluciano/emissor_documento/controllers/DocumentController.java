package br.com.dougluciano.emissor_documento.controllers;

import br.com.dougluciano.emissor_documento.entities.Document;
import br.com.dougluciano.emissor_documento.entities.dtos.DocumentResponseDTO;
import br.com.dougluciano.emissor_documento.entities.dtos.DocumentUploadRequestDTO;
import br.com.dougluciano.emissor_documento.entities.mapper.DocumentMapper;
import br.com.dougluciano.emissor_documento.services.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;
    private final DocumentMapper mapper;

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> listAll() {
        List<DocumentResponseDTO> documents = service.findAll()
                .stream()
                .map(mapper::toDocumentResponseDTO)
                .toList();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> findById(@PathVariable Long id) {
        Document document = service.findById(id);
        return ResponseEntity.ok(mapper.toDocumentResponseDTO(document));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        var documentContent = service.getDocumentContent(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentContent.response().contentType()))
                .body(documentContent.asByteArray());
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponseDTO> upload(
            @RequestBody @Valid DocumentUploadRequestDTO request,
            UriComponentsBuilder uriComponentsBuilder
            ){

        var toPersist = service.uploadDocument(
                request.title(),
                request.htmlContent()
        );

        var response = mapper.toDocumentResponseDTO(toPersist);

        var uri = uriComponentsBuilder.path("/documents/{id}").buildAndExpand(response).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
