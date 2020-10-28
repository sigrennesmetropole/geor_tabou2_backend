package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

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

    OperationEntity getById(Long idOperation);
}
