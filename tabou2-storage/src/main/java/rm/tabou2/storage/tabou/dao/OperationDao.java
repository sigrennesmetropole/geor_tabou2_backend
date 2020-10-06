package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.OperationEntity;

import java.util.Date;
import java.util.List;

public interface OperationDao extends CrudRepository<OperationEntity, Long>, JpaRepository<OperationEntity, Long> {

    /**
     * Retourne les opérations correspondants à la recherche, en exclut les operations à diffusion restreinte.
     *
     * @param keyword  mot clé de la recherche
     * @param pageable informations de pagination
     * @return liste des operations
     */
    @Query("SELECT o FROM OperationEntity o WHERE UPPER(o.nom) like UPPER(:keyword) and o.diffusionRestreinte = false")
    List<OperationEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);


    /**
     * Retourne les opérations correspondants à la recherche, en incluant les operations à diffusion restreinte.
     *
     * @param keyword  mot clé de la recherche
     * @param pageable informations de pagination
     * @return liste des operations
     */
    @Query("SELECT o FROM OperationEntity o WHERE UPPER(o.nom) like UPPER(:keyword)")
    List<OperationEntity> findByKeywordWithRestreints(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT o FROM OperationEntity o WHERE UPPER(o.nom) like UPPER(:nom) " +
            "and o.diffusionRestreinte = false " +
    "and UPPER(o.nature.libelle) like UPPER(:nature) "+
    "and UPPER(o.etapeOperation.libelle) like UPPER(:etape) "+
    "and UPPER(o.code) like UPPER(:code) " + "and UPPER(o.numAds) like UPPER(:numAds) " +
    "and o.autorisationDate BETWEEN :autorisationDateDebut and :autorisationDateFin")
    List<OperationEntity> search(@Param("nom") String nom, @Param("nature") String nature, @Param("etape") String etape,
                                 @Param("code") String code, @Param("numAds") String numAds,
                                 @Param("autorisationDateDebut") Date autorisationDateDebut, @Param("autorisationDateFin") Date autorisationDateFin, Pageable pageable);


}
