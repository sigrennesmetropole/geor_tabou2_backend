package rm.tabou2.storage.tabou.entity.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"typeFoncier"})
@Entity
@Table(name = "tabou_description_foncier")
public class DescriptionFoncierEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_description_foncier", nullable = false)
    private long id;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "taux")
    private Double taux;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_foncier")
    private TypeFoncierEntity typeFoncier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptionFoncierEntity that = (DescriptionFoncierEntity) o;
        return getId() == that.getId() && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getTaux(), that.getTaux());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getTaux());
    }
}
