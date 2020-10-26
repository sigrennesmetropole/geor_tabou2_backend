package rm.tabou2.storage.ddc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;

import java.util.List;

public interface PermisConstruireDao extends CrudRepository<PermisConstruireEntity, Long>, JpaRepository<PermisConstruireEntity, Long> {

    @Query("SELECT pc FROM PermisConstruireEntity pc WHERE UPPER(pc.numAds) like UPPER(:numAds)")
    List<PermisConstruireEntity> findAllByNumAds(@Param("numAds") String numAds);

}
