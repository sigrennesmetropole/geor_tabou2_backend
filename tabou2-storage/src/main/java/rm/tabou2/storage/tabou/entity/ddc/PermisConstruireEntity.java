package rm.tabou2.storage.tabou.entity.ddc;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;

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
    @Column(name = "num_ads", length = 255)
    private String numAds;

    @OrderBy
    @Basic
    @Column(name = "version_ads", length = 3)
    private String versionAds;

    @Basic
    @Column(name = "demandeur", length = 602)
    private String demandeur = null;

    @Basic
    @Column(name = "parcelles", length = 1000)
    private String parcelles = null;

    @Basic
    @Column(name = "decision", length = 100)
    private String decision = null;

    @Basic
    @Column(name = "date_depot_dossier")
    private LocalDateTime dateDepotDossier;

    @Basic
    @Column(name = "date_completude_dossier")
    private LocalDateTime dateCompletudeDossier;

    @Basic
    @Column(name = "ads_date")
    private LocalDateTime adsDate;

    @Basic
    @Column(name = "doc_date")
    private LocalDateTime docDate;

    @Basic
    @Column(name = "daact_date")
    private LocalDateTime datDate;

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

    public String buildPermisLibelle() {
        StringBuilder sb = new StringBuilder(258);

        sb.append(getNumAds());
        if (getVersionAds() != null) {
            sb.append("-").append(getVersionAds());
        }

        return sb.toString();
    }
}
