package rm.tabou2.storage.tabou.item;

import lombok.Data;

@Data
public class TiersAmenagementCriteria {
    private String libelle;
    private long operationId;
    private boolean asc;
    private String orderBy;
}
