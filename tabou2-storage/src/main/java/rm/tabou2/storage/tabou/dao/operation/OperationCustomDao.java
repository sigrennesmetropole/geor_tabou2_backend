package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.List;


public interface OperationCustomDao {

    Page<OperationEntity> searchOperations(OperationsCriteria operationsCriteria, Pageable pageable);

    List<String> searchCommunesByOperation(OperationEntity operation);

    OperationEntity searchParentSecteur(Long idSecteur);

}
