package rm.tabou2.storage.tabou.entity.programme;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

@EqualsAndHashCode(callSuper = true, exclude = {"typeEvenement", "programme"})
@Getter
@Setter
@ToString(exclude = {"typeEvenement", "programme"})
@Entity
@Table(name = "tabou_evenement_programme")
public class EvenementProgrammeEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evt_programme")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "systeme", nullable = false)
    private boolean systeme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_evt")
    public TypeEvenementEntity typeEvenement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programme")
    public ProgrammeEntity programme;

}
