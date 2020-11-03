package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;

import java.util.List;

public interface EtapeOperationDao extends CustomCrudRepository<EtapeOperationEntity, Long>, JpaRepository<EtapeOperationEntity, Long> {

    @Query("SELECT e FROM EtapeOperationEntity e WHERE UPPER(e.libelle) like UPPER(:keyword)")
    List<EtapeOperationEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    EtapeOperationEntity findByTypeAndCode(String type, String code);

    EtapeOperationEntity findByCode(String code);
}
