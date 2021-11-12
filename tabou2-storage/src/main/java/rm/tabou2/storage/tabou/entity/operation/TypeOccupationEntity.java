package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_type_occupation")
public class TypeOccupationEntity {

    @OrderBy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_occupation", nullable = false)
    private long id;

    @Basic
    @Column(name = "libelle", nullable = false, length = 120)
    private String libelle;

    @Basic
    @Column(name = "code", nullable = false, length = 20)
    private String code;
}
