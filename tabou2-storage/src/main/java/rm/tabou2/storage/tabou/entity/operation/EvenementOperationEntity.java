package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tabou_evenement_operation")
public class EvenementOperationEntity {

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

    @Basic
    @Column(name = "create_user")
    private String createUser;

    @Basic
    @Column(name = "create_date")
    private Date createDate;

    @Basic
    @Column(name = "modif_user")
    private String modifUser;

    @Basic
    @Column(name = "modif_date")
    private Date modifDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    public OperationEntity operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_evt")
    public TypeEvenementEntity typeEvenement;



}
