package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;

public interface ConsommationEspaceCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<ConsommationEspaceEntity> searchConsommationEspace(Pageable pageable);

}
