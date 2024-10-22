package rm.tabou2.storage.tabou.entity.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"typeContribution"})
@Entity
@Table(name = "tabou_contribution")
public class ContributionEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contribution", nullable = false)
    private long id;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_contribution")
    private TypeContributionEntity typeContribution;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContributionEntity that = (ContributionEntity) o;
        return getId() == that.getId() && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }
}
