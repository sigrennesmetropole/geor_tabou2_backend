package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import rm.tabou2.storage.tabou.entity.common.GenericCreateAuditableEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tabou_type_action_operation")
public class TypeActionOperationEntity extends GenericCreateAuditableEntity {

    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_action_operation", nullable = false)
    private long id;

    @Basic
    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;

    @Basic
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;
}
