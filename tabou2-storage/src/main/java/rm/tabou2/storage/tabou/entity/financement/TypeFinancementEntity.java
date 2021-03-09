package rm.tabou2.storage.tabou.entity.financement;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_type_financement")
public class TypeFinancementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_financement")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "est_aide")
    private boolean estAide;
}
