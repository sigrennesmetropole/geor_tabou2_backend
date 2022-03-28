package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.ActeurEntity;

public interface ActeurDao extends CrudRepository<ActeurEntity, Long>, JpaRepository<ActeurEntity, Long> {
}
