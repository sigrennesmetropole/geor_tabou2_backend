package rm.tabou2.storage.tabou.entity.tiers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_tiers", schema = "tabou2")
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
    @Column(name = "adresse_num")
    private String adresseNum;


    @Basic
    @Column(name = "adresse_rue")
    private String adresseRue;


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
    @Column(name = "contact")
    private String contact;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;

}
