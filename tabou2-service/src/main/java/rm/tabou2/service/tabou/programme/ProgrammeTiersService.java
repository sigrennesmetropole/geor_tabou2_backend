package rm.tabou2.service.tabou.programme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.AssociationTiersTypeTiers;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.dto.TiersTypeTiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;



public interface ProgrammeTiersService {

    AssociationTiersTypeTiers associateTiersToProgramme(long programmeId, long tiersId, long typeTiersId) throws AppServiceException;

    /**
     * Recherche les tiers des programmes à partir de paramètres.
     *
     * @param criteria criteria des tiers de l'amenagement
     * @param pageable    paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<TiersAmenagement> searchProgrammeTiers(TiersAmenagementCriteria criteria, Pageable pageable);

    AssociationTiersTypeTiers updateTiersAssociation(long programmeId, long programmeTiersId, TiersTypeTiers tiersTypeTiers) throws AppServiceException;

    void deleteTiersByProgrammeId(long programmeId, long programmeTiersId) throws AppServiceException;
}
