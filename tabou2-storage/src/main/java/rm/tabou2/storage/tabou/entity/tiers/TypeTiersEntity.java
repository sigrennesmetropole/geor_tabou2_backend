package rm.tabou2.storage.tabou.entity.tiers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericCreateAuditableEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_type_tiers")
public class TypeTiersEntity extends GenericCreateAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_tiers")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;

    @OneToMany(mappedBy = "typeTiers")
    public Set<OperationTiersEntity> operationsTiers;

    @OneToMany(mappedBy = "typeTiers")
    public Set<ProgrammeTiersEntity> programmeTiers;

}
