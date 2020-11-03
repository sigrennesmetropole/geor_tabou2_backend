package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

import java.util.List;

public interface EtapeProgrammeDao extends CustomCrudRepository<EtapeProgrammeEntity, Long>, JpaRepository<EtapeProgrammeEntity, Long> {

    @Query("SELECT e FROM EtapeProgrammeEntity e WHERE UPPER(e.libelle) like UPPER(:keyword)")
    List<EtapeProgrammeEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    EtapeProgrammeEntity findByTypeAndCode(String type, String code);

    EtapeProgrammeEntity findByCode(String code);
}
