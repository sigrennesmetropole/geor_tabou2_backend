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
    public AgapeoSuiviHabitat(long logementsLocatifAide, long logementsAccessAide,
                              long logementsLocatifRegulePrive, long logementsLocatifReguleHlm,
                              long logementsAccessMaitrise) {
        this.logementsLocatifAide = Math.toIntExact(logementsLocatifAide);
        this.logementsAccessAide = Math.toIntExact(logementsAccessAide);
        this.logementsLocatifRegulePrive = Math.toIntExact(logementsLocatifRegulePrive);
        this.logementsLocatifReguleHlm = Math.toIntExact(logementsLocatifReguleHlm);
        this.logementsAccessMaitrise = Math.toIntExact(logementsAccessMaitrise);
    }
}
