package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "tabou_programme")
public class ProgrammeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programme")
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
    @Column(name = "attribution_fonciere_annee")
    private int attributionFonciereAnnee;

    @Basic
    @Column(name = "attribution_date")
    private Date attributionDate;

    @Basic
    @Column(name = "commercialisation_date")
    private Date commercialisationDate;

    @Basic
    @Column(name = "num_ads")
    private String numAds;

    @Basic
    @Column(name = "ADS_date_prevu")
    private Date adsDatePrevu;

    @Basic
    @Column(name = "DOC_date_prevu")
    private Date docDatePrevu;

    @Basic
    @Column(name = "DAT_date_prevu")
    private Date datDatePrevu;

    @Basic
    @Column(name = "cloture_date")
    private Date clotureDate;

    @Basic
    @Column(name = "nb_logements")
    private int nbLogements;

    @Basic
    @Column(name = "logements_locat_aide_prevu")
    private int logementsLocatAidePrevu;

    @Basic
    @Column(name = "logements_access_aide_prevu")
    private int logementsAccessAidePrevu;

    @Basic
    @Column(name = "logements_locat_regule_prive_prevu")
    private int logementsLocatRegulePrivePrevu;

    @Basic
    @Column(name = "logements_locatif_regule_hlm_prevu")
    private int logementsLocatifReguleHlmPrevu;

    @Basic
    @Column(name = "logements_access_maitrise_prevu")
    private int logementsAccessMaitrisePrevu;

    @Basic
    @Column(name = "logements_access_libre_prevu")
    private int logementsAccessLibrePrevu;

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
    public Set<ProgrammeTiersEntity> programmeTiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etape_programme")
    public EtapeProgrammeEntity etapeProgramme;

}
