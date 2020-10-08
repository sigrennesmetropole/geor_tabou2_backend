package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "tabou_nature")
public class NatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nature")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;

    @OneToMany(mappedBy = "nature")
    public Set<OperationEntity> operations;


}
