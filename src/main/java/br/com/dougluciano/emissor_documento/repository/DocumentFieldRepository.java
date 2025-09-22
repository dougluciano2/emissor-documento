package br.com.dougluciano.emissor_documento.repository;

import br.com.dougluciano.emissor_documento.entities.DocumentField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentFieldRepository extends JpaRepository<DocumentField, Long> {

    Optional<DocumentField> findByPlaceholder(String placeholder);
}
