package br.com.dougluciano.emissor_documento.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_documents")
@Getter @Setter
public class Document extends AbstractFullEntity{

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "file_type", length = 100)
    private String fileType;

    @Column(name = "storage_path", nullable = false, unique = true)
    private String storagePath;
}
