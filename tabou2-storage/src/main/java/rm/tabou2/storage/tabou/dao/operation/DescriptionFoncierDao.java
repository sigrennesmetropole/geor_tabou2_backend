package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.DescriptionFoncierEntity;

public interface DescriptionFoncierDao extends CrudRepository<DescriptionFoncierEntity, Long>, JpaRepository<DescriptionFoncierEntity, Long> {
}
