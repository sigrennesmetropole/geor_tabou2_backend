package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "iris", schema = "demographie")
public class IrisEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @OrderBy
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
