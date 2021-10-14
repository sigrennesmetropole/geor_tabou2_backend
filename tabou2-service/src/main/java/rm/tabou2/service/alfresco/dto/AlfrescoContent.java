package rm.tabou2.service.alfresco.dto;

import lombok.Data;

@Data
public class AlfrescoContent {

    private String mimeType;
    private String mimeTypeName;
    private int sizeInBytes;
    private String encoding;

}
