package rm.tabou2.storage.sig.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

public interface SecteurFoncierDao extends CustomCrudRepository<SecteurFoncierEntity, Integer>, JpaRepository<SecteurFoncierEntity, Integer> {

}
