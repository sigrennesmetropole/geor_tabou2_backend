package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;

public interface MaitriseOuvrageDao extends CrudRepository<MaitriseOuvrageEntity, Long>, JpaRepository<MaitriseOuvrageEntity, Long> {

    MaitriseOuvrageEntity findByCode(String code);
}
