package rm.tabou2.service.bean.tabou.operation;

import lombok.*;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.dto.ModeAmenagement;
import rm.tabou2.service.dto.ConsommationEspace;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.Plh;
import rm.tabou2.service.dto.EntiteReferente;
import rm.tabou2.service.dto.InformationProgrammation;
import rm.tabou2.service.dto.VocationZA;
import rm.tabou2.service.dto.Contribution;
import rm.tabou2.service.dto.DescriptionFoncier;
import rm.tabou2.service.dto.TypeOccupation;
import rm.tabou2.service.dto.OutilFoncier;
import rm.tabou2.service.dto.Amenageur;
import rm.tabou2.service.dto.DescriptionConcertation;
import rm.tabou2.service.dto.DescriptionFinancementOperation;
import rm.tabou2.service.dto.ActionOperation;
import rm.tabou2.service.dto.Acteur;
import rm.tabou2.service.dto.Plui;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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

    private String description;

    private Boolean diffusionRestreinte;

    private Boolean secteur;

    private Date autorisationDate;

    private Date operationnelDate;

    private Date clotureDate;

    private BigDecimal surfaceTotale;

    private Integer nbLogementPrevu;

    private String ql1;

    private Boolean scot;

    private Double densiteScot;

    private String ql3;

    private Integer nbEntreprise;

    private Integer nbSalarie;

    private String numAds;

    private String objectifs;

    private Double pafTaux;

    private String outilAmenagement;

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

    private Set<Amenageur> amenageurs;

    private DescriptionConcertation concertation;

    private Set<DescriptionFinancementOperation> financements;

    private Set<ActionOperation> actions;

    private Set<Acteur> acteurs;

    private Plui plui;

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
                ", clotureDate=" + clotureDate +
                ", surfaceTotale=" + surfaceTotale +
                ", nbLogementPrevu=" + nbLogementPrevu +
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
                ", idEmprise=" + idEmprise +
                '}';
    }
}
