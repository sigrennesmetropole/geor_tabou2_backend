package rm.tabou2.service.bean.tabou.operation.suivi;

import lombok.Data;
import rm.tabou2.service.bean.tabou.common.Commentaire;

import java.util.List;

@Data
public class CommentairesOperation {
    private Commentaire commentaireMontage;
    private Commentaire commentairePlui;
    private Commentaire commentaireOperationnel;
    private Commentaire commentaireMoa;
    private List<Commentaire> autresCommentaires;
}
