package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;

import java.util.List;

public interface EvenementOperationDao extends CrudRepository<EvenementOperationEntity, Long>, JpaRepository<EvenementOperationEntity, Long> {

    @Query("SELECT e FROM EvenementOperationEntity e WHERE e.operation.id = :operationId")
    List<EvenementOperationEntity> findByOperationId(@Param("operationId") Long operationId, Pageable pageable);


}
