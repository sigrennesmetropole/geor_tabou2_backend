package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_action_operation")
public class ActionOperationEntity {
    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_action_operation", nullable = false)
    private long id;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_action_operation")
    private TypeActionOperationEntity typeAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private OperationEntity operation;

}
