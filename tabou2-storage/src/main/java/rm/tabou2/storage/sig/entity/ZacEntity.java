package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
