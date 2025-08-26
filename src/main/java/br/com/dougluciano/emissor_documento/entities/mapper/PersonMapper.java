package br.com.dougluciano.emissor_documento.entities.mapper;

import br.com.dougluciano.emissor_documento.entities.Person;
import br.com.dougluciano.emissor_documento.entities.dtos.PersonRequestDTO;
import br.com.dougluciano.emissor_documento.entities.dtos.PersonResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    // Instância para uso em locais onde a injeção não é possível (menos comum)
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    /**
     * Converte um DTO de requisição para uma Entidade Person.
     */
    Person toPerson(PersonRequestDTO dto);

    /**
     * Converte uma Entidade Person para um DTO de resposta.
     */
    PersonResponseDTO toPersonResponseDTO(Person person);

    /**
     * Atualiza uma entidade 'Person' existente (@MappingTarget) com os dados
     * de um DTO de requisição, ignorando o ID.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePersonFromDto(PersonRequestDTO dto, @MappingTarget Person person);
}
