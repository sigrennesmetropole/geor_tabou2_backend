package rm.tabou2.storage.tabou.item;

import lombok.Data;

@Data
public class AgapeoSuiviHabitat {

    private int logementsLocatifAide;

    private int logementsAccessAide;

    private int logementsLocatifRegulePrive;

    private int logementsLocatifReguleHlm;

    private int logementsAccessMaitrise;

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
