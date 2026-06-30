package rm.tabou2.storage.tabou.entity.logement;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Référentiel des types de logement (ex : social, libre, intermédiaire...).
@Getter
@Setter
@Entity
@Table(name = "tabou_type_logement")
public class TypeLogementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_logement")
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
}

