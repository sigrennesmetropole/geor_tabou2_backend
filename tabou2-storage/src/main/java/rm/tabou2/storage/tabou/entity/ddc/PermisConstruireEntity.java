package rm.tabou2.storage.tabou.entity.ddc;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tabou_pc_ddc")
public class PermisConstruireEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "num_ads")
    private String numAds;

    @Basic
    @Column(name = "demandeur")
    private String demandeur = null;

    @Basic
    @Column(name = "parcelles")
    private String parcelles = null;

    @Basic
    @Column(name = "decision")
    private String decision = null;

    @Basic
    @Column(name = "date_depot_dossier")
    private Date dateDepotDossier;

    @Basic
    @Column(name = "date_completude_dossier")
    private Date dateCompletudeDossier;

    @Basic
    @Column(name = "ads_date")
    private Date adsDate;

    @Basic
    @Column(name = "doc_date")
    private Date docDate;

    @Basic
    @Column(name = "daact_date")
    private Date datDate;

    @Basic
    @Column(name = "nbre_logement")
    private Integer nbreLogement = null;

    @Basic
    @Column(name = "surf_plancher_max")
    private Double surfPlancherMax = null;

    @Basic
    @Column(name = "surf_habitat")
    private Double surfHabitat = null;

    @Basic
    @Column(name = "surf_bureaux")
    private Double surfBureaux = null;

    @Basic
    @Column(name = "surf_commerces")
    private Double surfCommerces = null;

    @Basic
    @Column(name = "surf_industries")
    private Double surfIndustries = null;

    @Basic
    @Column(name = "surf_equip_pub")
    private Double surfEquipPub = null;

    @Basic
    @Column(name = "surf_autre")
    private Double surfAutre = null;

}
