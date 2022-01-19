package rm.tabou2.service.alfresco.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AlfrescoDocumentList {

    private AlfrescoPagination pagination;
    private Set<AlfrescoDocument> entries;

}


