package rm.tabou2.storage.tabou.entity.tiers;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_fonction_contact")
public class FonctionContactsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fonction_contact")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "code")
    private String code;
}
