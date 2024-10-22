package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "za", schema = "economie")
public class ZaEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "id_tabou")
    private Integer idTabou;

    @Basic
    @Column(name = "nomZa")
    private String nomZa;
}
