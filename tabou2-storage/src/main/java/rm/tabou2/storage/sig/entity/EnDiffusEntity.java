package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    @Basic
    @Column(name = "nature")
    private String nature;
}
