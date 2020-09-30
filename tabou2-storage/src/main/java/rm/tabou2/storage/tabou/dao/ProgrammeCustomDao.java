package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.ProgrammeEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

public interface ProgrammeCustomDao {

    /**
     * Recherche de programme à partir des paramètres.
     *
     * @param programmeCriteria paramètres des programmes
     * @param pageable paramètre lié à la pagination
     * @return Liste des programme correspondant à la recherche
     */
    Page<ProgrammeEntity> searchProgrammes(ProgrammeCriteria programmeCriteria, Pageable pageable);
}
