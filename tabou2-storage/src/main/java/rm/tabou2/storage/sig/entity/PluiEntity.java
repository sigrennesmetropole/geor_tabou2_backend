package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "plui_zone_urba", schema = "urba_foncier")
public class PluiEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "libelle")
    private String libelle;


}
