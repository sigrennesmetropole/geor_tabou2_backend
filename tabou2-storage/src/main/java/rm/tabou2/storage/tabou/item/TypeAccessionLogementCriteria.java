package rm.tabou2.storage.tabou.item;

import lombok.Builder;
import lombok.Data;
import rm.tabou2.storage.tabou.entity.logement.PorteeAccessionLogement;

@Data
@Builder
public class TypeAccessionLogementCriteria {

    private String libelle;

    private Boolean actifUniquement;

    private PorteeAccessionLogement portee;

}

