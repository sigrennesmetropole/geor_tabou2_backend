package rm.tabou2.storage.tabou.dao.agapeo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;

import java.util.List;

public interface AgapeoDao extends CrudRepository<AgapeoEntity, Long>, JpaRepository<AgapeoEntity, Long> {

    /**
     * liste des données agapeo par numAds
     * @param numAds numAds
     * @return List de Agapeo
     */
    @Query("SELECT a FROM AgapeoEntity a WHERE UPPER(a.numAds) like UPPER(:numAds)")
    List<AgapeoEntity> findAllByNumAds(@Param("numAds") String numAds);

}
