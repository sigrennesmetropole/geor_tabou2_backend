package rm.tabou2.storage.tabou.entity.operation;

import lombok.*;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

import jakarta.persistence.Basic;
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
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString(exclude = {"typeEvenement", "operation"})
@Entity
@Table(name = "tabou_evenement_operation")
public class EvenementOperationEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evt_operation")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "event_date")
    private Date eventDate;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "systeme", nullable = false)
    private Boolean systeme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_evt")
    public TypeEvenementEntity typeEvenement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    public OperationEntity operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvenementOperationEntity that = (EvenementOperationEntity) o;
        return getId() == that.getId() && Objects.equals(getEventDate(), that.getEventDate()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEventDate(), getDescription());
    }
}
