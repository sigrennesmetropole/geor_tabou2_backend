package rm.tabou2.storage.tabou.entity.operation;

import lombok.*;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"nextEtapes"})
@Entity
@Table(name = "tabou_etape_operation")
@NoArgsConstructor
@AllArgsConstructor
public class EtapeOperationEntity {

    public EtapeOperationEntity(long id, String code, String libelle) {
        this.id = id;
        this.libelle = libelle;
        this.code = code;
    }

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
            joinColumns = @JoinColumn(name = "id_etape_operation", referencedColumnName = "id_etape_operation"),
            inverseJoinColumns = @JoinColumn(name = "id_etape_operation_next", referencedColumnName = "id_etape_operation")
    )
    private Set<EtapeOperationEntity> nextEtapes;

}
