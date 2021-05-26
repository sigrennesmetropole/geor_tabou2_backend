package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oa_limite_intervention", schema = "urba_foncier")
public class EnDiffusEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "id_tabou")
    private Integer idTabou;

    @Basic
    @Column(name = "nomOpa")
    private String nom;
}
