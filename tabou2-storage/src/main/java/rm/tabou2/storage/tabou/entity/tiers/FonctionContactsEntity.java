package rm.tabou2.storage.tabou.entity.tiers;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "tabou_fonction_contact")
public class FonctionContactsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fonction_contact", nullable = false)
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle", length = 255, nullable = false)
    private String libelle;

    @Basic
    @Column(name = "code", length = 255, nullable = false)
    private String code;
}
