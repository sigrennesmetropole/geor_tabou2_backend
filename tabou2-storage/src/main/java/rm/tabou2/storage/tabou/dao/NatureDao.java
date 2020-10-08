package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;

import java.util.List;

public interface NatureDao extends CrudRepository<NatureEntity, Long>, JpaRepository<NatureEntity, Long> {

    @Query("SELECT n FROM NatureEntity n WHERE UPPER(n.libelle) like UPPER(:keyword)")
    List<NatureEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT n FROM NatureEntity n WHERE n.dateInactif is null")
    List<NatureEntity> findOnlyActive();



}
