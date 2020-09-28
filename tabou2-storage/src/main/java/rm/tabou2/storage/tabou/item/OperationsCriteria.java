package rm.tabou2.storage.tabou.item;

import lombok.Data;

import java.util.Date;


@Data
public class OperationsCriteria {
    private String nom;

    private String nature;

    private String etape;

    private Boolean diffusionRestreinte;

    private Boolean estSecteur;

    private String code;

    private String numAds;

    private Date autorisationDateDebut;

    private Date autorisationDateFin;

    private Date operationnelDateDebut;

    private Date operationnelDateFin;

    private Date clotureDateDebut;

    private Date clotureDateFin;

    private String tiers;

    private Integer start;

    private Integer resultsNumber;

    private String orderBy;

    private Boolean asc;

}
