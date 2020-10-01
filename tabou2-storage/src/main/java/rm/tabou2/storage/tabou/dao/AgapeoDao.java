package rm.tabou2.storage.tabou.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.habitat.AgapeoEntity;

import java.util.List;

public interface AgapeoDao extends CrudRepository<AgapeoEntity, Long>, JpaRepository<AgapeoEntity, Long> {

    /**
     * liste des données agapeo par numAds
     * @param numAds numAds
     * @return List de Agapeo
     */
    List<AgapeoEntity> findAgapeoEntitiesByNumAds(String numAds);

}
