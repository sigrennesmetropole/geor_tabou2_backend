package rm.tabou2.service.alfresco.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlfrescoUser {

    private String userId;

    private String password;

    public AlfrescoUser(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
