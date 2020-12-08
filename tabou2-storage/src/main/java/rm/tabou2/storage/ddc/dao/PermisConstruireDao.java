package rm.tabou2.storage.ddc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.ddc.item.PermisConstruireSuiviHabitat;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;

import java.util.List;

public interface PermisConstruireDao extends CrudRepository<PermisConstruireEntity, Long>, JpaRepository<PermisConstruireEntity, Long> {

    /**
     * liste des permis de construire par numAds
     * @param numAds numAds
     * @return List de PermisConstruire
     */
    @Query("SELECT pc FROM PermisConstruireEntity pc WHERE UPPER(pc.numAds) like UPPER(:numAds)")
    List<PermisConstruireEntity> findAllByNumAds(@Param("numAds") String numAds);

    /**
     * Récupère les informations permis de suivi habitat pour un programme
     * @param numAds    numads du programme
     * @return          PermisConstruireSuiviHabitat
     */
    @Query("SELECT new rm.tabou2.storage.ddc.item.PermisConstruireSuiviHabitat(" +
                "min(p.adsDate), " +
                "min(p.docDate), " +
                "min(p.datDate)) "
            + "FROM PermisConstruireEntity p WHERE UPPER(p.numAds) like UPPER(:numAds) group by p.numAds")
    PermisConstruireSuiviHabitat getPermisSuiviHabitatByNumAds(@Param("numAds") String numAds);

}
