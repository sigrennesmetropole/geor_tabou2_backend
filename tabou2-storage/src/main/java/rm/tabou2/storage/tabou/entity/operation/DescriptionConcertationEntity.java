package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

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
    private Date dateDebut;

    @Basic
    @Column(name = "date_fin")
    private Date dateFin;
}
