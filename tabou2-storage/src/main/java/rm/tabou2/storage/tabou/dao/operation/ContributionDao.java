package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.ContributionEntity;

public interface ContributionDao extends CrudRepository<ContributionEntity, Long>, JpaRepository<ContributionEntity, Long> {
}
