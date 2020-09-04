package rm.tabou2.storage.tabou.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.OperationTiersEntity;

public interface OperationTiersDao extends CrudRepository<OperationTiersEntity, Long>, JpaRepository<OperationTiersEntity, Long> {

}
