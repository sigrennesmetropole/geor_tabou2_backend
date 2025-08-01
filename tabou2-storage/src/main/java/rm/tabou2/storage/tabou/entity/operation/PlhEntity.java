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
    private LocalDateTime date;

    @Basic
    @Column(name = "description")
    private String description;
}
