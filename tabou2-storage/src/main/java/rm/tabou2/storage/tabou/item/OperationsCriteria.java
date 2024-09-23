package rm.tabou2.storage.tabou.item;

import lombok.Data;

import java.util.Date;


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

    private Date autorisationDateDebut;

    private Date autorisationDateFin;

    private Date operationnelDateDebut;

    private Date operationnelDateFin;

    private Date livraisonDateDebut;

    private Date livraisonDateFin;

    private Date clotureDateDebut;

    private Date clotureDateFin;

    private String tiers;

}
