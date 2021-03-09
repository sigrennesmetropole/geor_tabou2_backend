package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.dto.TiersAmenagement;
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
    Operation associateTiersToOperation(long operationId, long tiersId, long typeTiersId) throws AppServiceException;

    /**
     * Recherche les tiers des opération à partir de paramètres.
     *
     * @param criteria criteria des tiers de l'amenagement
     * @param pageable    paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<TiersAmenagement> searchOperationTiers(TiersAmenagementCriteria criteria, Pageable pageable) throws AppServiceException;
}
