package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

public interface OperationTiersService {

    /**
     * Associe une opération à un tiers et un type de tiers.
     *
     * @param operationId identifiant de l'opération
     * @param tiersId     identifiant du tiers
     * @param typeTiersId identifiant du type de tiers
     * @return
     * @throws AppServiceException
     */
    AssociationTiersTypeTiers associateTiersToOperation(long operationId, long tiersId, long typeTiersId) throws AppServiceException;

    /**
     * Recherche les tiers des opération à partir de paramètres.
     *
     * @param criteria criteria des tiers de l'amenagement
     * @param pageable paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<AssociationTiersTypeTiers> searchOperationTiers(TiersAmenagementCriteria criteria, Pageable pageable) throws AppServiceException;

    /**
     * Mise à jour de l'association operation-tiers
     *
     * @param operationId      identifiant de l'opération
     * @param operationTiersId identifiant de l'operationTiers
     * @param tiersTypeTiers   tierstypeTiers
     * @return tierstypetiers
     * @throws AppServiceException
     */
    AssociationTiersTypeTiers updateTiersAssociation(long operationId, long operationTiersId, TiersTypeTiers tiersTypeTiers) throws AppServiceException;

    /**
     * Suppression d'un tiers d'une opération.
     *
     * @param operationId      identifiant de l'opération
     * @param operationTiersId identifiant de l'opérationTiers
     * @throws AppServiceException
     */
    void deleteTiersByOperationId(long operationId, long operationTiersId) throws AppServiceException;
}
