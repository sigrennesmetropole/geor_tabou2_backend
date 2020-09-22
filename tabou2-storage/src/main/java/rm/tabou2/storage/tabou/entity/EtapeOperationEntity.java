package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "tabou_etape_operation")
public class EtapeOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etape_operation")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @OneToMany(mappedBy = "etapeOperation")
    public Set<OperationEntity> operations;

}
