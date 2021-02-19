package rm.tabou2.storage.sig.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

public interface SecteurSpeuDao extends CustomCrudRepository<SecteurSpeuEntity, Integer>, JpaRepository<SecteurSpeuEntity, Integer> {


}
