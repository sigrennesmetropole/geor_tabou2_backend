package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_description_financement_operation")
public class DescriptionFinancementOperationEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_description_financement_operation", nullable = false)
    private long id;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_financement_operation")
    private TypeFinancementOperationEntity typeFinancement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private OperationEntity operation;

}
