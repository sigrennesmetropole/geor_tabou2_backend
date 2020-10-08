package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeTiersEntity;

public interface ProgrammeTiersDao extends CrudRepository<ProgrammeTiersEntity, Long>, JpaRepository<ProgrammeTiersEntity, Long> {
}
