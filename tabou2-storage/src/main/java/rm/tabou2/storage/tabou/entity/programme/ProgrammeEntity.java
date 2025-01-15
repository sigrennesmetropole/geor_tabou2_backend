package rm.tabou2.storage.tabou.entity.programme;

import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

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

    @Basic
    @Column(name = "surface_shab")
    private Double surfaceSHAB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etape_programme")
    private EtapeProgrammeEntity etapeProgramme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private OperationEntity operation;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(
            name = "tabou_programme_type_plh",
            joinColumns = @JoinColumn(name = "id_programme", referencedColumnName = "id_programme"),
            inverseJoinColumns = @JoinColumn(name = "id_type_plh", referencedColumnName = "id_type_plh")
    )
    private Set<TypePLHEntity> plhs;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programme")
    private Set<AttributPLHEntity> attributsPLH;

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

    public void addTypePLHProgramme(TypePLHEntity typePLHEntity) {
        if (this.plhs == null) {
            this.plhs = new HashSet<>();
        }
        this.plhs.add(typePLHEntity);
    }

    public Optional<TypePLHEntity> lookupOptionalTypePLHById(long typePLHid) {
        if (this.plhs == null) {
            return Optional.empty();
        }
        return this.plhs.stream()
                .filter(typePLH -> typePLH.getId()== typePLHid)
                .findFirst();
    }

    public void addAttributPLHProgramme(AttributPLHEntity attributPLHEntity) {
        if (this.attributsPLH == null) {
            this.attributsPLH = new HashSet<>();
        }
        this.attributsPLH.add(attributPLHEntity);
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
