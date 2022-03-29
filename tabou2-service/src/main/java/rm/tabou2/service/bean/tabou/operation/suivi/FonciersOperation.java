package rm.tabou2.service.bean.tabou.operation.suivi;

import lombok.Data;

/**
 * Description des éléments fonciers d'une opération
 */
@Data
public class FonciersOperation {

    String foncierPrive;

    String foncierPublic;

    Double tauxFoncierPublic;
}
