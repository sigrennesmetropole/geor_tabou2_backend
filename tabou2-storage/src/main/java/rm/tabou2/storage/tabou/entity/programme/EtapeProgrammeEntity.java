package rm.tabou2.storage.tabou.entity.programme;

import lombok.*;

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
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"nextEtapes"})
@Entity
@Table(name = "tabou_etape_programme")
@NoArgsConstructor
@AllArgsConstructor
public class EtapeProgrammeEntity {

    public EtapeProgrammeEntity(long id, String code, String libelle) {
        this.id = id;
        this.libelle = libelle;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etape_programme")
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
            name = "tabou_etape_programme_workflow",
            joinColumns = @JoinColumn(name = "id_etape_programme", referencedColumnName = "id_etape_programme"),
            inverseJoinColumns = @JoinColumn(name = "id_etape_programme_next", referencedColumnName = "id_etape_programme")
    )
    private Set<EtapeProgrammeEntity> nextEtapes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtapeProgrammeEntity that = (EtapeProgrammeEntity) o;
        return getId() == that.getId() && Objects.equals(getLibelle(), that.getLibelle()) && getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLibelle(), getCode());
    }
}



