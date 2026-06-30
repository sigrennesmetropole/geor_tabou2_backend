package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.programme.ProgrammationHabitatEntity;

public interface ProgrammationHabitatDao extends CustomCrudRepository<ProgrammationHabitatEntity, Long>, JpaRepository<ProgrammationHabitatEntity, Long> {
}

