package rm.tabou2.storage.tabou.entity.logement;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Référentiel des types d'accession logement avec une portée (opération, programme ou secteur).
@Getter
@Setter
@Entity
@Table(name = "tabou_type_accession_logement")
public class TypeAccessionLogementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_accession_logement")
    private long id;

    @Basic
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Basic
    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;

    @Basic
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Basic
    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Basic
    @Column(name = "ordre", nullable = false)
    private int ordre;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tabou_type_accession_logement_portee",
            joinColumns = @JoinColumn(name = "id_type_accession_logement")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "portee", nullable = false, length = 20)
    private Set<PorteeAccessionLogement> portees = new HashSet<>();
}

