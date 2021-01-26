package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "tabou_etape_operation", schema = "tabou2")
public class EtapeOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etape_operation")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "mode")
    private String mode;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "remove_restriction")
    private boolean removeRestriction;

    @ManyToMany
    @JoinTable(
            name = "tabou_etape_operation_workflow",
            schema = "tabou2",
            joinColumns = @JoinColumn(name = "id_etape_operation", referencedColumnName = "id_etape_operation"),
            inverseJoinColumns = @JoinColumn(name = "id_etape_operation_next", referencedColumnName = "id_etape_operation")
    )
    private Set<EtapeOperationEntity> nextEtapes;

}
