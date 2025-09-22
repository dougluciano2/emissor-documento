package br.com.dougluciano.emissor_documento.repository;

import br.com.dougluciano.emissor_documento.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
}
