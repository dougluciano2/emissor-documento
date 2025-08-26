package br.com.dougluciano.emissor_documento.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter @Setter
@Entity
@Table(name = "tbl_persons")
public class Person extends AbstractFullEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "cpf")
    @CPF
    private String cpf;

    @Column(name = "address")
    private String address;

    @Column(name = "medical_diagnostics")
    private String medicalDiagnostics;
}
