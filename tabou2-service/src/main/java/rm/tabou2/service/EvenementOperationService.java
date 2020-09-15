package rm.tabou2.service;

import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.exception.AppServiceException;

import java.util.List;

public interface EvenementOperationService {

    /**
     * Retourne la liste des évènements d'une opération.
     *
     * @param operationId identifiant de l'opération
     * @return Liste des évènements
     */
    List<Evenement> getByOperationId(Long operationId);

    /**
     * Ajout d'un évènement sur une opération.
     *
     * @param evenement   évènement à ajouter
     * @param operationId identifiant de l'opération
     * @return évènement ajouté
     */
    Evenement addEvenement(Evenement evenement, Long operationId) throws AppServiceException;

    /**
     * Edite l'évènement d'une opération.
     *
     * @param evenement évènement à modifier
     * @return évènement modifié
     */
    Evenement editEvenement(Evenement evenement, Long operationId) throws AppServiceException;

}
