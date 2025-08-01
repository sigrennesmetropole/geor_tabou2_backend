package rm.tabou2.storage.tabou.item;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class OperationsCriteria {

    private String nom;

    private String nature;

    private String vocation;

    private String decision;

    private String modeAmenagement;

    private String outilAmenagement;

    private String maitriseOuvrage;

    private String consommationEspace;

    private String etape;

    private Boolean diffusionRestreinte;

    private Boolean estSecteur;

    private String code;

    private String numAds;

    private LocalDateTime autorisationDateDebut;

    private LocalDateTime autorisationDateFin;

    private LocalDateTime operationnelDateDebut;

    private LocalDateTime operationnelDateFin;

    private LocalDateTime livraisonDateDebut;

    private LocalDateTime livraisonDateFin;

    private LocalDateTime clotureDateDebut;

    private LocalDateTime clotureDateFin;

    private String tiers;

}
