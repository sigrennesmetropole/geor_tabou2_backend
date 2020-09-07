package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "etape_operation")
public class EtapeOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    public OperationEntity operation;

}
