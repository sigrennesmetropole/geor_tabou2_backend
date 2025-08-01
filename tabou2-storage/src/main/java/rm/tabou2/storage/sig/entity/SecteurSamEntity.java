package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "v_chargedoperation_secteur", schema = "urba_foncier")
public class SecteurSamEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @OrderBy
    @Basic
    @Column(name = "com_quart")
    private String nomSecteur;

    public SecteurSamEntity() {

    }

    public SecteurSamEntity(int id, String nomSecteur) {
        this.id = id;
        this.nomSecteur = nomSecteur;
    }
}
