package br.com.dougluciano.emissor_documento.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_templates")
@Getter @Setter
public class Template extends AbstractFullEntity{

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "storage_path", nullable = false, unique = true)
    private String storagePath;

}
