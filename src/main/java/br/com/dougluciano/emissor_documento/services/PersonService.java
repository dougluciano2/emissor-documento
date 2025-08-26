package br.com.dougluciano.emissor_documento.services;

import br.com.dougluciano.emissor_documento.entities.Person;
import br.com.dougluciano.emissor_documento.entities.dtos.PersonRequestDTO;
import br.com.dougluciano.emissor_documento.entities.dtos.PersonResponseDTO;
import br.com.dougluciano.emissor_documento.entities.mapper.PersonMapper;
import br.com.dougluciano.emissor_documento.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;


    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toPersonResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findById(Long id){
        var toResponse =  repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        return mapper.toPersonResponseDTO(toResponse);
    }

    @Transactional
    public PersonResponseDTO create(PersonRequestDTO request){
        var person = mapper.toPerson(request);
        Person save = repository.save(person);
        return mapper.toPersonResponseDTO(save);
    }

    @Transactional
    public PersonResponseDTO update(Long id, PersonRequestDTO request){
        var existis = repository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException());

        mapper.updatePersonFromDto(request, existis);

        var persisted = repository.save(existis);

        return mapper.toPersonResponseDTO(persisted);
    }

    public void delete(Long id){
        if (!repository.existsById(id)){
            throw new IllegalArgumentException();
        }
        repository.deleteById(id);
    }
}
