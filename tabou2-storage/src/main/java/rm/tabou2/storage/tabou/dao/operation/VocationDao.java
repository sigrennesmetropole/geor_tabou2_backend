package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;

public interface VocationDao extends CrudRepository<VocationEntity, Long>, JpaRepository<VocationEntity, Long> {

    VocationEntity findByCode(String code);
}
