package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;

public interface MaitriseOuvrageCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<MaitriseOuvrageEntity> searchMaitriseOuvrage(Pageable pageable);

}
