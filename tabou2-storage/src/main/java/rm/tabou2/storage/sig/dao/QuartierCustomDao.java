package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.QuartierEntity;
import rm.tabou2.storage.sig.item.QuartierCriteria;

public interface QuartierCustomDao {

    Page<QuartierEntity> searchQuartiers(QuartierCriteria quartierCriteria, Pageable pageable);

}
