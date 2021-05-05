package rm.tabou2.storage.tabou.dao.agapeo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;

public interface AgapeoCustomDao {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    Page<AgapeoEntity> searchAgapeo(String numAds, Pageable pageable);

}
