package br.com.dougluciano.emissor_documento.repository;

import br.com.dougluciano.emissor_documento.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByPersonIdOrderByMedicalRecordIndexDesc(Long personId);

    List<Document> findAllByPersonIdOrderByMedicalRecordIndexAsc(Long personId);
}
