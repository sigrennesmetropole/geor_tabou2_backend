package rm.tabou2.storage.tabou.entity.logement;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Regroupement de logements spécifiques par type d'accession.
@Getter
@Setter
@Entity
@Table(name = "tabou_logements_specifiques")
public class LogementsSpecifiquesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_logements_specifiques")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_accession_logement")
    private TypeAccessionLogementEntity typeAccessionLogement;

    @Basic
    @Column(name = "valeur")
    private Integer valeur;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_logements_specifiques", nullable = false)
    private List<LogementSpecifiqueEntity> logements = new ArrayList<>();
}
