package rm.tabou2.storage.tabou.dao.plh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;

public interface TypePLHDao extends CrudRepository<AttributPLHEntity, Long>, JpaRepository<AttributPLHEntity, Long> {
}
