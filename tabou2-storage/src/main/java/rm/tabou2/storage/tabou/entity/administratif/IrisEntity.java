package rm.tabou2.storage.tabou.entity.administratif;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "iris", schema = "demographie")
public class IrisEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "ccom")
    private String ccom;

    @Basic
    @Column(name = "code_iris")
    private String codeIris;

    @Basic
    @Column(name = "nmiris")
    private String nmiris;


}
