package br.com.dougluciano.emissor_documento.entities.mapper;

import br.com.dougluciano.emissor_documento.entities.Document;
import br.com.dougluciano.emissor_documento.entities.dtos.DocumentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(source = "medicalRecordIndex", target = "medicalRecordIndex")
    DocumentResponseDTO toDocumentResponseDTO(Document document);
}
