package rm.tabou2.service.alfresco.dto;

import lombok.Data;

@Data
public class AlfrescoPagination {

    private int count;
    private boolean hasMoreItems;
    private int totalItems;
    private int skipCount;
    private int maxItems;

}
