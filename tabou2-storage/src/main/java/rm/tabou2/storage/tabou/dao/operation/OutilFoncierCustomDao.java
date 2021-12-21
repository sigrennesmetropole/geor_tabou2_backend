package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.OutilFoncierEntity;
import rm.tabou2.storage.tabou.item.OutilFoncierCriteria;

public interface OutilFoncierCustomDao {

    Page<OutilFoncierEntity> searchOutilsFonciers(OutilFoncierCriteria criteria, Pageable pageabe);
}
