package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.util.List;

public interface ProgrammeDao extends CustomCrudRepository<ProgrammeEntity, Long>, JpaRepository<ProgrammeEntity, Long> {

    @Query("SELECT p FROM ProgrammeEntity p WHERE UPPER(p.nom) like UPPER(:keyword)")
    List<ProgrammeEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
