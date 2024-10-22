package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericCreateAuditableEntity;

import jakarta.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_type_occupation")
public class TypeOccupationEntity extends GenericCreateAuditableEntity {

    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_occupation", nullable = false)
    private long id;

    @Basic
    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;

    @Basic
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Basic
    @Column(name= "date_inactif")
    private Date dateInactif;
}
