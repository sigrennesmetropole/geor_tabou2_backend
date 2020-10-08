package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

import java.util.List;

public interface EtapeProgrammeDao extends CrudRepository<EtapeProgrammeEntity, Long>, JpaRepository<EtapeProgrammeEntity, Long> {

    @Query("SELECT e FROM EtapeProgrammeEntity e WHERE UPPER(e.libelle) like UPPER(:keyword)")
    List<EtapeProgrammeEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    EtapeProgrammeEntity findByType(String type);
}
