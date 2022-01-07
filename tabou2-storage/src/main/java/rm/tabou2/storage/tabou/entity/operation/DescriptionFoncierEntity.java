package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;

@Data
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private OperationEntity operation;
}
