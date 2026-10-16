package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.tabou.entity.operation.VocationPlaeEntity;

public interface VocationPlaeDao extends JpaRepository<VocationPlaeEntity, Long> {

    VocationPlaeEntity findByCode(String code);
}
