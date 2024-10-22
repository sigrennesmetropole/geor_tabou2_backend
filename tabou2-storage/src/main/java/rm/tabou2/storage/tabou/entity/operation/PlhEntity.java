package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tabou_plh")
public class PlhEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plh", nullable = false)
    private long id;

    @Basic
    @Column(name = "logement_prevu")
    private Integer logementsPrevus;

    @Basic
    @Column(name = "logement_livre")
    private Integer logementsLivres;

    @Basic
    @Column(name = "date")
    private Date date;

    @Basic
    @Column(name = "description")
    private String description;
}
