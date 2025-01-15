package rm.tabou2.storage.tabou.dao.plh;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;

public interface AttributPLHDao extends CustomCrudRepository<AttributPLHEntity, Long>, JpaRepository<AttributPLHEntity, Long> {
}
