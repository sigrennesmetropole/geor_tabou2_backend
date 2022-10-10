package rm.tabou2.storage.tabou.entity.operation;

import lombok.Getter;
import lombok.Setter;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "tabou_operation")
public class OperationEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operation")
    private long id;

    @Basic
    @Column(name = "code")
    private String code;

    @OrderBy
    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "operation")
    private String operation;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "diffusion_restreinte")
    private boolean diffusionRestreinte;

    @Basic
    @Column(name = "est_secteur")
    private Boolean secteur;

    @Basic
    @Column(name = "autorisation_date")
    private Date autorisationDate;

    @Basic
    @Column(name = "operationnel_date")
    private Date operationnelDate;

    @Basic
    @Column(name = "cloture_date")
    private Date clotureDate;

    @Basic
    @Column(name = "surface_totale")
    private Double surfaceTotale;

    @Basic
    @Column(name = "nb_logement_prevu")
    private Integer nbLogementsPrevu;

    @Basic
    @Column(name = "QL1")
    private String ql1;

    @Basic
    @Column(name = "scot")
    private Boolean scot;

    @Basic
    @Column(name = "densite_scot")
    private Double densiteScot;

    @Basic
    @Column(name = "QL3")
    private String ql3;

    @Basic
    @Column(name = "nb_entreprise")
    private Integer nbEntreprise;

    @Basic
    @Column(name = "nb_salarie")
    private Integer nbSalarie;

    @Basic
    @Column(name = "num_ads")
    private String numAds;

    @Basic
    @Column(name = "objectifs")
    private String objectifs;

    @Basic
    @Column(name = "paf_taux")
    private Double pafTaux;

    @Basic
    @Column(name = "outil_amenagement")
    private String outilAmenagement;

    @Basic
    @Column(name = "etude")
    private String etude;

    @Basic
    @Column(name = "localisation")
    private String localisation;

    @Basic
    @Column(name = "usage_actuel")
    private String usageActuel;

    @Basic
    @Column(name = "avancement_administratif")
    private String avancementAdministratif;

    @Basic
    @Column(name = "environnement")
    private String environnement;

    @Basic
    @Column(name = "surface_realisee")
    private Double surfaceRealisee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etape_operation")
    public EtapeOperationEntity etapeOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nature")
    public NatureEntity nature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vocation")
    public VocationEntity vocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_decision")
    public DecisionEntity decision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maitrise_ouvrage")
    public MaitriseOuvrageEntity maitriseOuvrage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mode_amenagement")
    public ModeAmenagementEntity modeAmenagement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consommation_espace")
    public ConsommationEspaceEntity consommationEspace;

    @OneToMany(mappedBy = "operation")
    public Set<OperationTiersEntity> operationsTiers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_operation")
    public Set<EvenementOperationEntity> evenements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<ProgrammeEntity> programmes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plh")
    private PlhEntity plh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entite_referente")
    private EntiteReferenteEntity entiteReferente;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<InformationProgrammationEntity> informationsProgrammation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vocation_za")
    private VocationZAEntity vocationZa;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<ContributionEntity> contributions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<DescriptionFoncierEntity> descriptionsFoncier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_occupation")
    private TypeOccupationEntity typeOccupation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_outil_foncier")
    private OutilFoncierEntity outilFoncier;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<AmenageurEntity> amenageurs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_description_concertation")
    private DescriptionConcertationEntity concertation;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<DescriptionFinancementOperationEntity> financements;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<ActionOperationEntity> actions;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operation")
    private Set<ActeurEntity> acteurs;

    @Embedded
    @AttributeOverride(name = "densiteOap", column = @Column(name = "densite_oap"))
    @AttributeOverride(name = "pluiDisposition", column = @Column(name = "plui_disposition"))
    @AttributeOverride(name = "pluiAdaptation", column = @Column(name = "plui_adaptation"))
    private Plui plui;

    public void addEvenementOperation(EvenementOperationEntity evenementOperationEntity) {
        this.evenements.add(evenementOperationEntity);
    }

    public Optional<EvenementOperationEntity> lookupEvenementById(long idEvenementOperation) {
        return this.evenements.stream()
                .filter(ep -> ep.getId() == idEvenementOperation)
                .findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationEntity that = (OperationEntity) o;
        return getId() == that.getId() && getCode().equals(that.getCode()) && getNom().equals(that.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getNom());
    }

    @Override
    public String toString() {
        return "OperationEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", operation='" + operation + '\'' +
                ", description='" + description + '\'' +
                ", diffusionRestreinte=" + diffusionRestreinte +
                ", secteur=" + secteur +
                ", autorisationDate=" + autorisationDate +
                ", operationnelDate=" + operationnelDate +
                ", clotureDate=" + clotureDate +
                ", surfaceTotale=" + surfaceTotale +
                ", nbLogementsPrevu=" + nbLogementsPrevu +
                ", ql1='" + ql1 + '\'' +
                ", scot=" + scot +
                ", densiteScot=" + densiteScot +
                ", ql3='" + ql3 + '\'' +
                ", nbEntreprise=" + nbEntreprise +
                ", nbSalarie=" + nbSalarie +
                ", numAds='" + numAds + '\'' +
                ", objectifs='" + objectifs + '\'' +
                ", pafTaux=" + pafTaux +
                ", outilAmenagement='" + outilAmenagement + '\'' +
                ", etude='" + etude + '\'' +
                ", localisation='" + localisation + '\'' +
                ", usageActuel='" + usageActuel + '\'' +
                ", avancementAdministratif='" + avancementAdministratif + '\'' +
                ", environnement='" + environnement + '\'' +
                ", surfaceRealisee=" + surfaceRealisee +
                '}';
    }
}
