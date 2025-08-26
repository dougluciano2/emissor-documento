package br.com.dougluciano.emissor_documento.controllers;

import br.com.dougluciano.emissor_documento.entities.Person;
import br.com.dougluciano.emissor_documento.entities.dtos.PersonRequestDTO;
import br.com.dougluciano.emissor_documento.entities.dtos.PersonResponseDTO;
import br.com.dougluciano.emissor_documento.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> create(@Valid @RequestBody PersonRequestDTO person){
        var persist = service.create(person);

        URI uriLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(persist.id())
                .toUri();

        return ResponseEntity.created(uriLocation).body(persist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> update(@Valid @PathVariable Long id, @RequestBody PersonRequestDTO person){
        var persist = service.update(id, person);

        return ResponseEntity.ok(persist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> delete(@Valid @PathVariable Long id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
