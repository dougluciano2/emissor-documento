package br.com.dougluciano.emissor_documento.entities;

import jakarta.persistence.*;
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

    @Column(name = "medical_record_index", nullable = false)
    private Integer medicalRecordIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
