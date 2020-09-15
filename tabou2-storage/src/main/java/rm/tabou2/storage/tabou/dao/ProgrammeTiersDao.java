package rm.tabou2.storage.tabou.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.ProgrammeTiersEntity;

public interface ProgrammeTiersDao extends CrudRepository<ProgrammeTiersEntity, Long>, JpaRepository<ProgrammeTiersEntity, Long> {
}
