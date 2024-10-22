package rm.tabou2.storage.tabou.entity.tiers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_tiers")
public class TiersEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tiers")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "est_prive")
    private Boolean estPrive;

    @Basic
    @Column(name = "adresse")
    private String adresse;

    @Basic
    @Column(name = "adresse_cp")
    private String adresseCp;

    @Basic
    @Column(name = "adresse_ville")
    private String adresseVille;

    @Basic
    @Column(name = "telephone")
    private String telephone;

    @Basic
    @Column(name = "telecopie")
    private String telecopie;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "site_web")
    private String siteWeb;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tiers", nullable = false)
    private Set<ContactTiersEntity> contacts;

}
