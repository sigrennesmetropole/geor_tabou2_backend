package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import rm.tabou2.storage.tabou.entity.common.GenericCreateAuditableEntity;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tabou_entite_referente")
public class EntiteReferenteEntity extends GenericCreateAuditableEntity {
    @Id
    @OrderBy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entite_referente", nullable = false)
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
