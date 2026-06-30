package rm.tabou2.storage.tabou.dao.logement;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.logement.LogementsSpecifiquesEntity;

public interface LogementsSpecifiquesDao extends CustomCrudRepository<LogementsSpecifiquesEntity, Long>, JpaRepository<LogementsSpecifiquesEntity, Long> {
}

