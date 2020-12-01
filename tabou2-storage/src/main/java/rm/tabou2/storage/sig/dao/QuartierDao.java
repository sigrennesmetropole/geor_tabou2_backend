package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.sig.entity.QuartierEntity;

import java.util.List;

public interface QuartierDao extends CrudRepository<QuartierEntity, Integer>, JpaRepository<QuartierEntity, Integer> {

    @Query("SELECT q FROM QuartierEntity q WHERE UPPER(q.nom) like UPPER(:keyword)")
    List<QuartierEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
