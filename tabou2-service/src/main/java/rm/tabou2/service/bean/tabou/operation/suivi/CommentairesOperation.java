package rm.tabou2.service.bean.tabou.operation.suivi;

import lombok.Data;
import rm.tabou2.service.bean.tabou.common.Commentaire;

import java.util.List;

/**
 * Ensemble des commentaires d'une opération
 */
@Data
public class CommentairesOperation {
    private Commentaire commentaireMontage;
    private Commentaire commentairePlui;
    private Commentaire commentaireOperationnel;
    private Commentaire commentaireMoa;
    private Commentaire commentaireDauh;
    private Commentaire commentairePisu;
    private Commentaire commentaireCommune;
    private Commentaire commentaireMandataire;
    private Commentaire commentaireMobiliteAutre;
    private List<Commentaire> autresCommentaires;
}
