package rm.tabou2.service.alfresco.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AlfrescoEntry {

    private List<String> aspectNames;
    private Date createdAt;
    private boolean isFolder;
    private boolean isFile;
    private CreatedByUser createdByUser;
    private Date modifiedAt;
    private ModifiedByUser modifiedByUser;
    private String name;
    private String id;
    private String nodeType;
    private AlfrescoProperties properties;
    private AlfrescoContent content;
    private String parentId;

}
