package rm.tabou2.storage.tabou.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "operation_tiers")
public class OperationTiersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "create_user")
    private String createUser;

    @Basic
    @Column(name = "create_date")
    private Date createDate;

    @Basic
    @Column(name = "modif_user")
    private String modifUser;

    @Basic
    @Column(name = "modif_date")
    private Date modifDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    public OperationEntity operation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programme")
    public ProgrammeEntity programme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tiers")
    public TiersEntity tiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_tiers")
    public TypeTiersEntity typeTiers;

}
