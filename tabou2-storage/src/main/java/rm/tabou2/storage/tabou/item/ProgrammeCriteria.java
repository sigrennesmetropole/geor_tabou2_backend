package rm.tabou2.storage.tabou.item;

import lombok.Data;

import java.util.Date;

@Data
public class ProgrammeCriteria {

    private String nom;
    private String etape;
    private Boolean diffusionRestreinte;
    private String code;
    private String numAds;
    private String nomOperation;
    private long operationId;
    private String natureOperation;
    private Date clotureDateDebut;
    private Date clotureDateFin;
    private String tiers;
    private Integer attributionFonciereAnneeDebut;
    private Integer attributionFonciereAnneeFin;
    private Date attributionDateDebut;
    private Date attributionDateFin;
    private Date commercialisationDateDebut;
    private Date commercialisationDateFin;
    private Date adsDateDebut;
    private Date adsDateFin;
    private Date livraisonDateDebut;
    private Date livraisonDateFin;
    private Date docDateDebut;
    private Date docDateFin;
    private Date datDateDebut;
    private Date datDateFin;
    private Boolean logementsAides;
}
