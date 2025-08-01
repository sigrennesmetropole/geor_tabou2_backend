package rm.tabou2.service.bean.tabou.operation.suivi;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EcheancierOperation {

    private String typeEvenement;

    private String description;

    private LocalDateTime eventDate;

    private String modifUser;

}
