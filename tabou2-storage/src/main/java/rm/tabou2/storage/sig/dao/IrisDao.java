package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.IrisEntity;

import java.util.List;

public interface IrisDao extends CustomCrudRepository<IrisEntity, Integer>, JpaRepository<IrisEntity, Integer> {

    @Query("SELECT i FROM IrisEntity i WHERE UPPER(i.ccom) like UPPER(:keyword)")
    List<IrisEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
