package rm.tabou2.service.alfresco.dto;

import lombok.Data;

@Data
public class AlfrescoUser {

    private String userId;

    private String password;

    public AlfrescoUser() {
        //Empty constructor
    }

    public AlfrescoUser(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
