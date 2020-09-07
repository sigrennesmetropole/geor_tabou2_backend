package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "tiers")
public class TiersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "correspondant")
    private String correspondant;

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

    @OneToMany(mappedBy = "tiers")
    public Set<OperationTiersEntity> operationsTiers;

}
