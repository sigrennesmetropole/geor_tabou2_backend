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


        this.logementsLocatifAide = logementsLocatifAide != null ? Math.toIntExact(logementsLocatifAide) : 0;
        this.logementsAccessAide = logementsAccessAide != null ? Math.toIntExact(logementsAccessAide) : 0;
        this.logementsLocatifRegulePrive = logementsLocatifRegulePrive != null ? Math.toIntExact(logementsLocatifRegulePrive) : 0;
        this.logementsLocatifReguleHlm = logementsLocatifReguleHlm != null ? Math.toIntExact(logementsLocatifReguleHlm) : 0;
        this.logementsAccessMaitrise = logementsAccessMaitrise != null ? Math.toIntExact(logementsAccessMaitrise) : 0;

    }
}
