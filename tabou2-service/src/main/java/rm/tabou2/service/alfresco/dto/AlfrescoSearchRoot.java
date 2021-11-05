package rm.tabou2.service.alfresco.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlfrescoSearchRoot {

    private AlfrescoSearchQuery query;
    private List<String> include;
    private AlfrescoPaging paging;
    private List<AlfrescoSort> sort;

}
