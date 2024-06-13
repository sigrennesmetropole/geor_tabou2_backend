package rm.tabou2.service.bean.tabou.operation.suivi;

import lombok.Data;
import java.util.Date;

@Data
public class EcheancierOperation {

    private String typeEvenement;

    private String description;

    private Date eventDate;

    private String modifUser;

}
