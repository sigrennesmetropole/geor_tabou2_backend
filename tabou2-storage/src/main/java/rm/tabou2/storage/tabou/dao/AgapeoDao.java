package rm.tabou2.storage.tabou.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.habitat.AgapeoEntity;

public interface AgapeoDao extends CrudRepository<AgapeoEntity, Long>, JpaRepository<AgapeoEntity, Long> {



}
