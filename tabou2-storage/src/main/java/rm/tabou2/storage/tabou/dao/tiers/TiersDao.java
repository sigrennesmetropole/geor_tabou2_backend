package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;

public interface TiersDao extends CrudRepository<TiersEntity, Long>, JpaRepository<TiersEntity, Long> {


}
