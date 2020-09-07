package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.EvenementEntity;

import java.util.List;

public interface EvenementDao extends CrudRepository<EvenementEntity, Long>, JpaRepository<EvenementEntity, Long> {

    @Query("SELECT e FROM EvenementEntity e WHERE e.operation.id = :operationId")
    List<EvenementEntity> findByOperationId(@Param("operationId") Long operationId, Pageable pageable);


}
