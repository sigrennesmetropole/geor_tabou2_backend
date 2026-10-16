package rm.tabou2.storage.tabou.entity.operation;

import java.time.LocalDateTime;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "tabou_renouvellement_eco_plae")
public class RenouvellementEconomiquePlaeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Basic
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Basic
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Basic
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
}
