package rm.tabou2.storage.tabou.entity.programme;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_programme_tiers")
public class ProgrammeTiersEntity extends GenericAuditableEntity {

    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programme_tiers")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programme")
    public ProgrammeEntity programme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tiers")
    public TiersEntity tiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_tiers")
    public TypeTiersEntity typeTiers;


}
