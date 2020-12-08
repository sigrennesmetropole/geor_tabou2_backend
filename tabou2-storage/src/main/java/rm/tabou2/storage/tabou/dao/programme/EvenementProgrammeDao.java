package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;

public interface EvenementProgrammeDao extends CustomCrudRepository<EvenementProgrammeEntity, Long>, JpaRepository<EvenementProgrammeEntity, Long> {
}
