package rm.tabou2.storage.sig.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.SecteurSamEntity;

public interface SecteurSamDao extends CustomCrudRepository<SecteurSamEntity, Integer>, JpaRepository<SecteurSamEntity, Integer> {
}
