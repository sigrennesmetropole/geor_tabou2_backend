package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "programme")
public class ProgrammeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "programme")
    private String programme;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "attribution_fonciere_aAnnee")
    private String attributionFonciereAnnee;

    @Basic
    @Column(name = "attribution_date")
    private Date attributionDate;

    @Basic
    @Column(name = "autorisation_date")
    private String autorisationDate;

    @Basic
    @Column(name = "commercialisation_date")
    private Date commercialisationDate;

    @Basic
    @Column(name = "demarrage_date")
    private Date demarrageDate;

    @Basic
    @Column(name = "livraison_date")
    private Date livraisonDate;

    @Basic
    @Column(name = "cloture_date")
    private Date clotureDate;

    @Basic
    @Column(name = "logements_total")
    private int logementsTotal;

    @Basic
    @Column(name = "logements_access_libre")
    private int logementsAccessLibre;

    @Basic
    @Column(name = "logements_access_maitrise")
    private int logementsAccessMaitrise;

    @Basic
    @Column(name = "logements_locat_regul")
    private int logementsLocatRegul;

    @Basic
    @Column(name = "logements_pls")
    private int logementsPls;

    @Basic
    @Column(name = "logements_access_aide")
    private int logementsAccessAide;

    @Basic
    @Column(name = "logements_locat_aide")
    private int logementsLocatAide;

    @Basic
    @Column(name = "create_user")
    private String createUser;

    @Basic
    @Column(name = "create_date")
    private Date createDate;

    @Basic
    @Column(name = "modif_user")
    private String modifUser;

    @Basic
    @Column(name = "modif_date")
    private Date modifDate;

    @OneToMany(mappedBy = "programme")
    public Set<OperationTiersEntity> operationsTiers;

    @OneToMany(mappedBy = "programme")
    public Set<EtapeProgrammeEntity> etapes;

}
