package rm.tabou2.storage.tabou.entity.operation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"operations"})
@Entity
@Table(name = "tabou_nature")
public class NatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nature")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "date_inactif")
    private LocalDateTime dateInactif;

    @OneToMany(mappedBy = "nature")
    public Set<OperationEntity> operations;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NatureEntity that = (NatureEntity) o;
        return getId() == that.getId() && Objects.equals(getLibelle(), that.getLibelle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLibelle());
    }
}
