package rm.tabou2.storage.tabou.entity.programme;

import lombok.*;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

import javax.persistence.*;
import java.util.Date;

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
    private Date eventDate;

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
