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
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericCreateAuditableEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_type_foncier")
public class TypeFoncierEntity extends GenericCreateAuditableEntity {

    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_foncier", nullable = false)
    private long id;

    @Basic
    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;

    @Basic
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Basic
    @Column(name = "date_inactif")
    private LocalDateTime dateInactif;
}
