package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.operation.EntiteRMEntity;
import rm.tabou2.storage.tabou.item.EntiteRMCriteria;

public interface EntiteRMCustomDao {
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<EntiteRMEntity> searchEntitesRM(EntiteRMCriteria criteria, Pageable pageable);
}
