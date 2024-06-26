package rm.tabou2.storage.tabou.dao.ddc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;

public interface PermisConstruireCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<PermisConstruireEntity> searchPermisConstruire(String numAds, Pageable pageable);

}
