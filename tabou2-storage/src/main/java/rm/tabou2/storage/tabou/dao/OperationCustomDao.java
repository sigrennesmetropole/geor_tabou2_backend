package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;


public interface OperationCustomDao {

    Page<OperationEntity> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);

}
