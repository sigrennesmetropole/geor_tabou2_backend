package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_evenement_operation")
public class EvenementOperationEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evt_operation")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "event_date")
    private Date eventDate;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "systeme")
    private Boolean systeme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    public OperationEntity operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_evt")
    public TypeEvenementEntity typeEvenement;



}
