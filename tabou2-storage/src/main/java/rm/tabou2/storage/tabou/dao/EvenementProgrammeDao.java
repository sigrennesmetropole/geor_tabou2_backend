package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.EvenementProgrammeEntity;

import java.util.List;

public interface EvenementProgrammeDao extends CrudRepository<EvenementProgrammeEntity, Long>, JpaRepository<EvenementProgrammeEntity, Long> {

    @Query("SELECT e FROM EvenementProgrammeEntity e WHERE e.programme.id = :programmeId")
    List<EvenementProgrammeEntity> findByProgrammeId(@Param("programmeId") Long programmeId, Pageable pageable);


}
