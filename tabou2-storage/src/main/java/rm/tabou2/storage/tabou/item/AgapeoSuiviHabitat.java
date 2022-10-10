package rm.tabou2.storage.tabou.item;

import lombok.Data;

@Data
public class AgapeoSuiviHabitat {

    private Integer logementsLocatifAide;

    private Integer logementsAccessAide;

    private Integer logementsLocatifRegulePrive;

    private Integer logementsLocatifReguleHlm;

    private Integer logementsAccessMaitrise;

    public AgapeoSuiviHabitat() {

    }
    public AgapeoSuiviHabitat(Long logementsLocatifAide, Long logementsAccessAide,
                              Long logementsLocatifRegulePrive, Long logementsLocatifReguleHlm,
                              Long logementsAccessMaitrise) {


        this.logementsLocatifAide = logementsLocatifAide != null ? Math.toIntExact(logementsLocatifAide) : null;
        this.logementsAccessAide = logementsAccessAide != null ? Math.toIntExact(logementsAccessAide) : null;
        this.logementsLocatifRegulePrive = logementsLocatifRegulePrive != null ? Math.toIntExact(logementsLocatifRegulePrive) : null;
        this.logementsLocatifReguleHlm = logementsLocatifReguleHlm != null ? Math.toIntExact(logementsLocatifReguleHlm) : null;
        this.logementsAccessMaitrise = logementsAccessMaitrise != null ? Math.toIntExact(logementsAccessMaitrise) : null;

    }
}
