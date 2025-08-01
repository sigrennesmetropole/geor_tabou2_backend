package rm.tabou2.storage.tabou.entity.tiers;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

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
    private LocalDateTime dateInactif;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tiers", nullable = false)
    private Set<ContactTiersEntity> contacts;

}
