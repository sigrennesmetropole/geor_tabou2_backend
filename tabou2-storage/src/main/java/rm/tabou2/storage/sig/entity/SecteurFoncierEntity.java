package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "negociateurfoncier_secteur", schema = "urba_foncier")
public class SecteurFoncierEntity {

    @Id
    @Column(name = "objectid")
    private Long id;

    @OrderBy
    @Basic
    @Column(name = "negociateur")
    private String negociateur;

    public SecteurFoncierEntity() {

    }

    public SecteurFoncierEntity(Long id, String negociateur) {
        this.id = id;
        this.negociateur = negociateur;
    }

}
