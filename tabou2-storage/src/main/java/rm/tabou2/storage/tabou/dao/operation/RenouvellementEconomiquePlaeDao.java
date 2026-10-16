package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.tabou.entity.operation.RenouvellementEconomiquePlaeEntity;

public interface RenouvellementEconomiquePlaeDao extends JpaRepository<RenouvellementEconomiquePlaeEntity, Long> {

    RenouvellementEconomiquePlaeEntity findByCode(String code);
}
