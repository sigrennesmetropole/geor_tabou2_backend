package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.operation.RenouvellementEconomiquePlaeEntity;

public interface RenouvellementEconomiquePlaeCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<RenouvellementEconomiquePlaeEntity> searchRenouvellementsEconomiquesPlae(Boolean actifUniquement, Pageable pageable);

}
