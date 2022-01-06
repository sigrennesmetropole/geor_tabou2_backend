package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeProgrammationEntity;
import rm.tabou2.storage.tabou.item.TypeProgrammationCriteria;

public interface TypeProgrammationCustomDao {

    Page<TypeProgrammationEntity> searchTypesProgrammations(TypeProgrammationCriteria criteria, Pageable pageable);
}
