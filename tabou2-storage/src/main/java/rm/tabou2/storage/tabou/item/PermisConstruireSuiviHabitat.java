package rm.tabou2.storage.tabou.item;

import lombok.Data;

import java.util.Date;

@Data
public class PermisConstruireSuiviHabitat {

    private Date adsDate;

    private Date docDate;

    private Date datDate;

    public PermisConstruireSuiviHabitat() {

    }

    public PermisConstruireSuiviHabitat(Date adsDate, Date docDate, Date datDate) {
        this.adsDate = adsDate;
        this.docDate = docDate;
        this.datDate = datDate;
    }
}
