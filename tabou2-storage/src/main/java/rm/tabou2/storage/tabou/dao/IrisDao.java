package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.administratif.IrisEntity;

import java.util.List;

public interface IrisDao extends CrudRepository<IrisEntity, Long>, JpaRepository<IrisEntity, Long> {

    @Query("SELECT i FROM IrisEntity i WHERE UPPER(i.ccom) like UPPER(:keyword)")
    List<IrisEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
