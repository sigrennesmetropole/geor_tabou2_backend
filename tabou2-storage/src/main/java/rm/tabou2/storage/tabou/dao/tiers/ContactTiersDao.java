package rm.tabou2.storage.tabou.dao.tiers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;

public interface ContactTiersDao extends CrudRepository<ContactTiersEntity, Long>, JpaRepository<ContactTiersEntity, Long> {
}
