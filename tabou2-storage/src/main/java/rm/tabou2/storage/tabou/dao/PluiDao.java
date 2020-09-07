package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.administratif.PluiEntity;

import java.util.List;

public interface PluiDao extends CrudRepository<PluiEntity, Long>, JpaRepository<PluiEntity, Long> {

    @Query("SELECT p FROM PluiEntity p WHERE UPPER(p.nom) like UPPER(:keyword)")
    List<PluiEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
