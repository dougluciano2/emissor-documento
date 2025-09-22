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
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentController {

    private final DocumentService service;
    private final DocumentMapper mapper;

    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> listAll(
            @RequestParam(name = "personId", required = false) Long personId
    ) {
        List<Document> documents;

        if(personId != null){
            documents = service.findByMedicalRecordByPerson(personId);
        } else {
            documents = service.findAll();
        }

        List<DocumentResponseDTO> responseDto = documents.stream()
                .map(mapper::toDocumentResponseDTO)
                .toList();

        return ResponseEntity.ok(responseDto);
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

    @PostMapping
    public ResponseEntity<DocumentResponseDTO> upload(
            @RequestBody @Valid DocumentUploadRequestDTO request,
            UriComponentsBuilder uriComponentsBuilder
            ){


        var toPersist = service.uploadDocument(request);

        var response = mapper.toDocumentResponseDTO(toPersist);

        var uri = uriComponentsBuilder.path("/documents/{id}")
                .buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
