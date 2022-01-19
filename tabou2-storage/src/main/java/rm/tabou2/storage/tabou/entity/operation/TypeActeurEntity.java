package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericCreateAuditableEntity;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_type_acteur")
public class TypeActeurEntity extends GenericCreateAuditableEntity {

    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_acteur", nullable = false)
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
