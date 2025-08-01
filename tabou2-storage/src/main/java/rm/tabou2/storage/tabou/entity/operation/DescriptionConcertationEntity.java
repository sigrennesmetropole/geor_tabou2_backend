package rm.tabou2.storage.tabou.entity.operation;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tabou_description_concertation")
public class DescriptionConcertationEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_description_concertation", nullable = false)
    private long id;

    @Basic
    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Basic
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
}
