package br.com.dougluciano.emissor_documento.entities.mapper;

import br.com.dougluciano.emissor_documento.entities.Template;
import br.com.dougluciano.emissor_documento.entities.dtos.TemplateResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    TemplateResponseDTO toTemplateResponseDTO(Template template);
}
