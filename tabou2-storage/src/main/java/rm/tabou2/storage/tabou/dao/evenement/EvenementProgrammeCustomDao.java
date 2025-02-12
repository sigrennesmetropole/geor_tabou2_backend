package rm.tabou2.storage.tabou.dao.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

public interface EvenementProgrammeCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<EvenementProgrammeEntity> searchEvenementsProgramme(Long programmeId, TypeEvenementCriteria typeEvenementCriteria, Pageable pageable);

}
