package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeContributionEntity;
import rm.tabou2.storage.tabou.item.TypeContributionCriteria;

public interface TypeContributionCustomDao {

    Page<TypeContributionEntity> searchTypesContributions(TypeContributionCriteria criteria, Pageable pageable);
}
