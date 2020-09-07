package rm.tabou2.storage.tabou.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.ProgrammeEntity;

public interface ProgrammeDao extends CrudRepository<ProgrammeEntity, Long>, JpaRepository<ProgrammeEntity, Long> {

}
