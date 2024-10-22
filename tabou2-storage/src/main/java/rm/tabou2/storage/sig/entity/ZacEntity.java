package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "zac", schema = "urba_foncier")
public class ZacEntity {

    @Id
    @Column(name = "id_zac")
    private Integer id;

    @Basic
    @Column(name = "id_tabou")
    private Integer idTabou;

    @Basic
    @Column(name = "nomZac")
    private String nomZac;
}
