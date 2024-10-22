package rm.tabou2.storage.tabou.entity.operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"typeAmenageur"})
@Entity
@Table(name = "tabou_amenageur")
public class AmenageurEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_amenageur", nullable = false)
    private long id;

    @Basic
    @Column(name = "nom", length = 255)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_amenageur")
    private TypeAmenageurEntity typeAmenageur;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmenageurEntity that = (AmenageurEntity) o;
        return getId() == that.getId() && Objects.equals(getNom(), that.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom());
    }
}
