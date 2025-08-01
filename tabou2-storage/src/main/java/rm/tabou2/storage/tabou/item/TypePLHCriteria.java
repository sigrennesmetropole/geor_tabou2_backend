package rm.tabou2.storage.tabou.item;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TypePLHCriteria {

    private String libelle;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    private Long programmeId;

    private Boolean selectionnable;

}
