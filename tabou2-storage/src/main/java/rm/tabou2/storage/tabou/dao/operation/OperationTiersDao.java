package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;

import java.util.List;

public interface OperationTiersDao extends CrudRepository<OperationTiersEntity, Long>, JpaRepository<OperationTiersEntity, Long> {


    @Query("SELECT o FROM OperationTiersEntity o WHERE o.operation.id = :operationId")
    List<OperationTiersEntity> findByOperationId(@Param("operationId") long operationId);

}
