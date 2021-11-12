package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_entites_rm")
public class EntiteRMEntity {
    @Id
    @OrderBy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entite_rm", nullable = false)
    private long id;

    @Basic
    @Column(name = "libelle", nullable = false, length = 255)
    private String libelle;

    @Basic
    @Column(name = "code", nullable = false, length = 255)
    private String code;
}
