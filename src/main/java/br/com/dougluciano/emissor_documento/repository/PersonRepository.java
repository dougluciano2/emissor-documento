package br.com.dougluciano.emissor_documento.repository;

import br.com.dougluciano.emissor_documento.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
