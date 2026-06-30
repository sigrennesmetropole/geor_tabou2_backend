package rm.tabou2.service.bean.tabou.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rm.tabou2.service.dto.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationIntermediaire {

    private Long id;

    private String code;

    private String nom;

    private String operation;

    private Long parentId;

    private String description;

    private Boolean diffusionRestreinte;

    private Boolean secteur;

    private LocalDateTime annulationDate;

    private LocalDateTime autorisationDate;

    private LocalDateTime operationnelDate;

    private LocalDateTime livraisonDate;

    private LocalDateTime clotureDate;

    private BigDecimal surfaceTotale;

    private float aireGeoHa;

    private float aireGeoHaParent;

    private Integer nbLogementsPrevu;

    private Integer nbLogementsHFV;

    private Integer hebergementResidSeniorPrevu;

    private Integer hebergementResidSeniorRealise;

    private String ql1;

    private Boolean scot;

    private Double densiteScot;

    private String ql3;

    private Integer nbEntreprise;

    private Integer nbSalarie;

    private String numAds;

    private String objectifs;

    private Double pafTaux;

    private String etude;

    private String localisation;

    private String usageActuel;

    private String avancementAdministratif;

    private String environnement;

    private Double surfaceRealisee;

    private Long idEmprise;

    private Etape etape;

    private Nature nature;

    private Vocation vocation;

    private Decision decision;

    private MaitriseOuvrage maitriseOuvrage;

    private ModeAmenagement modeAmenagement;

    private ConsommationEspace consommationEspace;

    private Set<TiersAmenagement> operationsTiers;

    private Set<Evenement> evenements = new HashSet<>();

    private Set<Programme> programmes = new HashSet<>();

    private Plh plh;

    private EntiteReferente entiteReferente;

    private Set<InformationProgrammation> informationsProgrammation;

    private VocationZA vocationZa;

    private Set<Contribution> contributions;

    private Set<DescriptionFoncier> descriptionsFoncier;

    private TypeOccupation typeOccupation;

    private OutilFoncier outilFoncier;

    private OutilAmenagement outilAmenagement;

    private Set<Amenageur> amenageurs;

    private DescriptionConcertation concertation;

    private Set<DescriptionFinancementOperation> financements;

    private String elementsFinanciers;

    private Boolean financementPPI;

    private Set<ActionOperation> actions;

    private Set<Acteur> acteurs;

    private Plui plui;

    private ProjetUrbain projetUrbain;

    private List<LogementsSpecifiques> logementsSpecifiques;

    private String mos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationIntermediaire that = (OperationIntermediaire) o;
        return getId().equals(that.getId()) && getCode().equals(that.getCode()) && Objects.equals(getNom(), that.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getNom());
    }

    @Override
    public String toString() {
        return "OperationIntermediaire{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", operation='" + operation + '\'' +
                ", description='" + description + '\'' +
                ", diffusionRestreinte=" + diffusionRestreinte +
                ", secteur=" + secteur +
                ", autorisationDate=" + autorisationDate +
                ", operationnelDate=" + operationnelDate +
                ", livraisonDate=" + livraisonDate +
                ", clotureDate=" + clotureDate +
                ", surfaceTotale=" + surfaceTotale +
                ", aireGeoHa=" + aireGeoHa +
                ", nbLogementsPrevu=" + nbLogementsPrevu +
                ", nbLogementsHFV=" + nbLogementsHFV +
                ", hebergementResidSeniorPrevu=" + hebergementResidSeniorPrevu +
                ", hebergementResidSeniorRealise=" + hebergementResidSeniorRealise +
                ", ql1='" + ql1 + '\'' +
                ", scot=" + scot +
                ", densiteScot=" + densiteScot +
                ", ql3='" + ql3 + '\'' +
                ", nbEntreprise=" + nbEntreprise +
                ", nbSalarie=" + nbSalarie +
                ", numAds='" + numAds + '\'' +
                ", objectifs='" + objectifs + '\'' +
                ", pafTaux=" + pafTaux +
                ", etude='" + etude + '\'' +
                ", localisation='" + localisation + '\'' +
                ", usageActuel='" + usageActuel + '\'' +
                ", avancementAdministratif='" + avancementAdministratif + '\'' +
                ", environnement='" + environnement + '\'' +
                ", surfaceRealisee=" + surfaceRealisee +
                ", idEmprise=" + idEmprise +
                '}';
    }
}
