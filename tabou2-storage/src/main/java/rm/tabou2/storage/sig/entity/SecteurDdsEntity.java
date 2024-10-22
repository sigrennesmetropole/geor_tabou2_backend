package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "instructeur_secteur", schema = "urba_foncier")
public class SecteurDdsEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @OrderBy
    @Basic
    @Column(name = "secteur")
    private String secteur;

    public SecteurDdsEntity() {

    }

    public SecteurDdsEntity(int id, String secteur) {
        this.id = id;
        this.secteur = secteur;
    }



}
