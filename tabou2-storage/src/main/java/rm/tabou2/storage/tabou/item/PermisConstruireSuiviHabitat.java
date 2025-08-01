package rm.tabou2.storage.tabou.item;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PermisConstruireSuiviHabitat {

    private LocalDateTime adsDate;

    private LocalDateTime docDate;

    private LocalDateTime datDate;

    public PermisConstruireSuiviHabitat() {

    }

    public PermisConstruireSuiviHabitat(LocalDateTime adsDate, LocalDateTime docDate, LocalDateTime datDate) {
        this.adsDate = adsDate;
        this.docDate = docDate;
        this.datDate = datDate;
    }
}
