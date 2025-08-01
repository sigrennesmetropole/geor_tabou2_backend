package rm.tabou2.storage.tabou.item;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProgrammeCriteria {

    private String nom;
    private String etape;
    private Boolean diffusionRestreinte;
    private String code;
    private String numAds;
    private String nomOperation;
    private long operationId;
    private long programmeId;
    private String natureOperation;
    private LocalDateTime clotureDateDebut;
    private LocalDateTime clotureDateFin;
    private String tiers;
    private Integer attributionFonciereAnneeDebut;
    private Integer attributionFonciereAnneeFin;
    private LocalDateTime attributionDateDebut;
    private LocalDateTime attributionDateFin;
    private LocalDateTime commercialisationDateDebut;
    private LocalDateTime commercialisationDateFin;
    private LocalDateTime adsDateDebut;
    private LocalDateTime adsDateFin;
    private LocalDateTime livraisonDateDebut;
    private LocalDateTime livraisonDateFin;
    private LocalDateTime annulationDate;
    private LocalDateTime docDateDebut;
    private LocalDateTime docDateFin;
    private LocalDateTime datDateDebut;
    private LocalDateTime datDateFin;
    private Boolean logementsAides;
}
