package rm.tabou2.storage.tabou.dao.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

public interface EvenementOperationCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<EvenementOperationEntity> searchEvenementsOperation(Long operationId,
          TypeEvenementCriteria typeEvenementCriteria, Pageable pageable);

}
