package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;

public interface ConsommationEspaceDao extends CrudRepository<ConsommationEspaceEntity, Long>, JpaRepository<ConsommationEspaceEntity, Long> {

    ConsommationEspaceEntity findByCode(String code);
}
