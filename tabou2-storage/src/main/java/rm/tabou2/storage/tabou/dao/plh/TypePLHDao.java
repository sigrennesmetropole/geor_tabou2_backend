package rm.tabou2.storage.tabou.dao.plh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

public interface TypePLHDao extends CrudRepository<TypePLHEntity, Long>, JpaRepository<TypePLHEntity, Long> {
}
