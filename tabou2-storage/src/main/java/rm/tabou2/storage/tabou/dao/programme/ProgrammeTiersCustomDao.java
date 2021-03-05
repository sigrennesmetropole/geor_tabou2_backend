package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;

public interface ProgrammeTiersCustomDao {

    /**
     * Recherche les tiers des programes à partir de paramètres.
     *
     * @param programmeId identifiant de l'opération
     * @param libelleType libellé du type de tiers
     * @param codeType    code du type de tiers
     * @param pageable    paramètres de pagination
     * @return Tiers correspondants à la recherche
     */
    Page<ProgrammeTiersEntity> searchProgrammesTiers(String libelleType, String codeType, Long programmeId, Pageable pageable);
}
