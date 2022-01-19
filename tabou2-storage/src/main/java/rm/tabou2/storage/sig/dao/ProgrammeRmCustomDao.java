package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.item.ProgrammeCriteria;

/**
 * Interface du DAO d'accès aux Programme Rennes Metropole (oa_programme).
 */
public interface ProgrammeRmCustomDao {

    /**
     * Recherche des emprises de programme non suives.
     *
     * @param operationId identifiant de l'operation
     * @param
     * @param pageable paramètres de la pagination
     * @return emprises correspondant aux paramètres de recherche
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<ProgrammeRmEntity> searchEmprisesNonSuivies(Long operationId,  String nom, Pageable pageable);

    Page<ProgrammeRmEntity> searchProgrammesWithinOperation(ProgrammeCriteria programmeCriteria, Pageable pageable);

}
