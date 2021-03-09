package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

public interface EtapeOperationCustomDao {

    Page<EtapeOperationEntity> searchEtapeOperations(EtapeCriteria etapeCriteria, Pageable pageable);

}
