package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chargedoperation_secteur", schema = "urba_foncier")
public class SecteurSamEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @OrderBy
    @Basic
    @Column(name = "nom_secteur")
    private String nomSecteur;

    public SecteurSamEntity() {

    }

    public SecteurSamEntity(int id, String nomSecteur) {
        this.id = id;
        this.nomSecteur = nomSecteur;
    }

}
