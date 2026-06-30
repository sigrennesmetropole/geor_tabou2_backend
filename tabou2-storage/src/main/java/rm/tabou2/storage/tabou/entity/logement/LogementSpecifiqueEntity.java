package rm.tabou2.storage.tabou.entity.logement;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Logement spécifique : valeurs prévues et réalisées pour un type de logement donné.
@Getter
@Setter
@Entity
@Table(name = "tabou_logement_specifique")
public class LogementSpecifiqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_logement_specifique")
    private long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_logement", nullable = false)
    private TypeLogementEntity typeLogement;

    @Basic
    @Column(name = "valeur_prevue")
    private Integer valeurPrevue;

    @Basic
    @Column(name = "valeur_realisee")
    private Integer valeurRealisee;
}
