package rm.tabou2.storage.ddc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;

public interface PermisConstruireDao extends CrudRepository<PermisConstruireEntity, Long>, JpaRepository<PermisConstruireEntity, Long> {



}
