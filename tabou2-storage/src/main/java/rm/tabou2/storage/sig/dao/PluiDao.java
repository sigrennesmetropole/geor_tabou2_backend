package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.sig.entity.PluiEntity;

import java.util.List;

public interface PluiDao extends CrudRepository<PluiEntity, Integer>, JpaRepository<PluiEntity, Integer> {

    @Query("SELECT p FROM PluiEntity p WHERE UPPER(p.libelle) like UPPER(:keyword)")
    List<PluiEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
