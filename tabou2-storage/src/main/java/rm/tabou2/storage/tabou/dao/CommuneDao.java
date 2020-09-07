package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.administratif.CommuneEntity;

import java.util.List;

public interface CommuneDao extends CrudRepository<CommuneEntity, Long>, JpaRepository<CommuneEntity, Long> {

    @Query("SELECT c FROM CommuneEntity c WHERE UPPER(c.nom) like UPPER(:keyword)")
    List<CommuneEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);


}
