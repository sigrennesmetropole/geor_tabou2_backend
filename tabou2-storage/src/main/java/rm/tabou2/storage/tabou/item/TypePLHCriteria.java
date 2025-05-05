package rm.tabou2.storage.tabou.item;

import lombok.Data;

import java.util.Date;

@Data
public class TypePLHCriteria {

    private String libelle;

    private Date dateDebut;

    private Date dateFin;

    private Long programmeId;

    private Boolean selectionnable;

}
