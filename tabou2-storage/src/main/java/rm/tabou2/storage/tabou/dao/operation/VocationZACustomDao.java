package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.VocationZAEntity;
import rm.tabou2.storage.tabou.item.VocationZACriteria;

public interface VocationZACustomDao {

    Page<VocationZAEntity> searchVocationsZA(VocationZACriteria criteria, Pageable pageable);
}
