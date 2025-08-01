package rm.tabou2.storage.tabou.entity.evenement;

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
@Table(name = "tabou_type_evenement")
public class TypeEvenementEntity extends GenericCreateAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_evt")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "systeme", nullable = false)
    private boolean systeme;

    @Basic
    @Column(name = "date_inactif")
    private LocalDateTime dateInactif;


}
