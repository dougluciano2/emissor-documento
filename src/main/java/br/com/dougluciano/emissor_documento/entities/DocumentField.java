package br.com.dougluciano.emissor_documento.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_document_fields")
@Getter @Setter
public class DocumentField extends AbstractFullEntity{

    @Column(nullable = false, unique = true)
    private String placeholder;

    @Column(nullable = false)
    private String description;

    @Column(name = "expression", nullable = false)
    private String expression;
}
