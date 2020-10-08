package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;

import java.util.List;

public interface TiersDao extends CrudRepository<TiersEntity, Long>, JpaRepository<TiersEntity, Long> {

    @Query("SELECT t FROM TiersEntity t WHERE UPPER(t.nom) like UPPER(:keyword)")
    List<TiersEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM TiersEntity t WHERE UPPER(t.nom) like UPPER(:keyword) and t.dateInactif is null")
    List<TiersEntity> findOnlyActiveByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
