package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;
import rm.tabou2.storage.tabou.item.TiersAmenagementCriteria;

public interface ProgrammeTiersCustomDao {

    /**
     * Recherche les tiers des programes à partir de paramètres.
     *
     * @param criteria critères de recherche
     * @param pageable    paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<ProgrammeTiersEntity> searchProgrammesTiers(TiersAmenagementCriteria criteria, Pageable pageable);
}
