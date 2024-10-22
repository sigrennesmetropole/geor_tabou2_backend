package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "plui_zone_urba", schema = "urba_foncier")
public class PluiEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    public PluiEntity() {
    }

    public PluiEntity(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }


}
