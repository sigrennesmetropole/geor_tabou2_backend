package rm.tabou2.service.mapper.tabou.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.dto.Decision;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
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

    public Etape etape;

    public Nature nature;

    public Vocation vocation;

    public Decision decision;

    public rm.tabou2.service.dto.MaitriseOuvrage maitriseOuvrage;

    public rm.tabou2.service.dto.ModeAmenagement modeAmenagement;

    public rm.tabou2.service.dto.ConsommationEspace consommationEspace;

    public Set<rm.tabou2.service.dto.TiersAmenagement> operationsTiers;

    public Set<rm.tabou2.service.dto.Evenement> evenements = new HashSet<>();

    private Set<rm.tabou2.service.dto.Programme> programmes = new HashSet<>();

    private rm.tabou2.service.dto.Plh plh;

    private rm.tabou2.service.dto.EntiteReferente entiteReferente;

    private Set<rm.tabou2.service.dto.InformationProgrammation> informationsProgrammation;

    private rm.tabou2.service.dto.VocationZA vocationZa;

    private Set<rm.tabou2.service.dto.Contribution> contributions;

    private Set<rm.tabou2.service.dto.DescriptionFoncier> descriptionsFoncier;

    private rm.tabou2.service.dto.TypeOccupation typeOccupation;

    private rm.tabou2.service.dto.OutilFoncier outilFoncier;

    private Set<rm.tabou2.service.dto.Amenageur> amenageurs;

    private rm.tabou2.service.dto.DescriptionConcertation concertation;

    private Set<rm.tabou2.service.dto.DescriptionFinancementOperation> financements;

    private Set<rm.tabou2.service.dto.ActionOperation> actions;

    private Set<rm.tabou2.service.dto.Acteur> acteurs;

    private rm.tabou2.service.dto.Plui plui;

}
