package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.TypeEvenementEntity;
import rm.tabou2.storage.tabou.entity.TypeTiersEntity;

import java.util.List;

public interface TypeTiersDao extends CrudRepository<TypeTiersEntity, Long>, JpaRepository<TypeTiersEntity, Long> {

    @Query("SELECT t FROM TypeTiersEntity t WHERE UPPER(t.libelle) like UPPER(:keyword)")
    List<TypeTiersEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM TypeTiersEntity t WHERE UPPER(t.libelle) like UPPER(:keyword) and t.dateInactif is null")
    List<TypeTiersEntity> findOnlyActiveByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
