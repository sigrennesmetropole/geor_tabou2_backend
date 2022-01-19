package rm.tabou2.storage.tabou.item;

import lombok.Data;

@Data
public class TiersAmenagementCriteria {

    private String libelle;
    private String codeTypeTiers;
    private long operationId;
    private long programmeId;
    private boolean asc;
    private String orderBy;

}
