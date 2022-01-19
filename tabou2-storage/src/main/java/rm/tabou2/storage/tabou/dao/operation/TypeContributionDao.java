package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.TypeContributionEntity;

public interface TypeContributionDao extends CrudRepository<TypeContributionEntity, Long>,
        JpaRepository<TypeContributionEntity, Long> {
}
