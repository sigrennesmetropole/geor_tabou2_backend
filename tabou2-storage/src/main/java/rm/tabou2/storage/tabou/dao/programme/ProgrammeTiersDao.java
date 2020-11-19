package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;

import java.util.List;

public interface ProgrammeTiersDao extends CrudRepository<ProgrammeTiersEntity, Long>, JpaRepository<ProgrammeTiersEntity, Long> {

    //@Query("SELECT p FROM ProgrammeTiersEntity p WHERE UPPER(p.nom) like UPPER(:keyword)")
    List<ProgrammeTiersEntity> findByProgrammeId(long programmeId);
}
