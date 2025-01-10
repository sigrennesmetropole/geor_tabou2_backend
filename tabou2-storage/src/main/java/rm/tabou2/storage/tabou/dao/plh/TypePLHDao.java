package rm.tabou2.storage.tabou.dao.plh;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

public interface TypePLHDao extends CustomCrudRepository<TypePLHEntity, Long>, JpaRepository<TypePLHEntity, Long> {
}
