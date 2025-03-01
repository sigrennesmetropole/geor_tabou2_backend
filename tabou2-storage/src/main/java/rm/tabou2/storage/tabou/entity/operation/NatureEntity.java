package rm.tabou2.storage.tabou.entity.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
    private Date dateInactif;

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
