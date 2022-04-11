package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.EnDiffusEntity;

import java.util.List;

public interface EnDiffusDao extends CustomCrudRepository<EnDiffusEntity, Integer>, JpaRepository<EnDiffusEntity, Integer> {


    @Query("SELECT e FROM EnDiffusEntity e WHERE " +
            "e.idTabou is NULL AND " +
            "e.nature is NOT NULL AND " +
            "e.nature NOT LIKE '' AND " +
            "UPPER(e.nom) LIKE UPPER(:nom)")
    List<EnDiffusEntity> findAllByName(@Param("nom") String nom, Pageable pageable);

    EnDiffusEntity getByIdTabou(Integer idTabou);

    int countAllByIdTabouIsNullAndNomIsLikeIgnoreCase(String nom);

}
