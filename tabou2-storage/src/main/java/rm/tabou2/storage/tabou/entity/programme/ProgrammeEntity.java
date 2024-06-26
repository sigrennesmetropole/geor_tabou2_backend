package rm.tabou2.storage.tabou.entity.programme;

import lombok.*;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.*;


@Getter
@Setter
@ToString(exclude = {"evenements", "etapeProgramme", "operation"})
@Entity
@Table(name = "tabou_programme")
public class ProgrammeEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programme")
    private long id;

    @Basic
    @Column(name = "code")
    private String code;

    @OrderBy
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
    private Integer attributionFonciereAnnee;

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
    @Column(name = "date_annulation")
    private Date dateAnnulation;

    @Basic
    @Column(name = "date_livraison")
    private Date dateLivraison;

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
    @Column(name = "diffusion_restreinte")
    private boolean diffusionRestreinte;

    @Basic
    @Column(name = "nb_logements")
    private int nbLogements;

    @Basic
    @Column(name = "logements_locatif_aide_prevu")
    private int logementsLocatifAidePrevu;

    @Basic
    @Column(name = "logements_access_aide_prevu")
    private int logementsAccessAidePrevu;

    @Basic
    @Column(name = "logements_locatif_regule_prive_prevu")
    private int logementsLocatifRegulePrivePrevu;

    @Basic
    @Column(name = "logements_locatif_regule_hlm_prevu")
    private int logementsLocatifReguleHlmPrevu;

    @Basic
    @Column(name = "logements_access_maitrise_prevu")
    private int logementsAccessMaitrisePrevu;

    @Basic
    @Column(name = "logements_access_libre_prevu")
    private int logementsAccessLibrePrevu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etape_programme")
    private EtapeProgrammeEntity etapeProgramme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private OperationEntity operation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_programme")
    private Set<EvenementProgrammeEntity> evenements = new HashSet<>();

    public void addEvenementProgramme(EvenementProgrammeEntity evenementProgrammeEntity) {
        this.evenements.add(evenementProgrammeEntity);
    }

    public Optional<EvenementProgrammeEntity> lookupEvenementById(long idEvenementProgramme) {
        return this.evenements.stream()
                .filter(ep -> ep.getId() == idEvenementProgramme)
                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgrammeEntity that = (ProgrammeEntity) o;
        return getId() == that.getId() && getCode().equals(that.getCode()) && getNom().equals(that.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getNom());
    }
}
