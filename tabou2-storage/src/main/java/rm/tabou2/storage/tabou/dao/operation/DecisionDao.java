package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.DecisionEntity;

public interface DecisionDao extends CrudRepository<DecisionEntity, Long>, JpaRepository<DecisionEntity, Long> {

    DecisionEntity findByCode(String code);
}
