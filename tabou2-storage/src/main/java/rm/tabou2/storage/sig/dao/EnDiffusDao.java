package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.EnDiffusEntity;

import java.util.List;

public interface EnDiffusDao extends CustomCrudRepository<EnDiffusEntity, Integer>, JpaRepository<EnDiffusEntity, Integer> {

    List<EnDiffusEntity> findAllByIdTabouIsNullAndNomIsLikeIgnoreCase(String nom, Pageable pageable);

    int countAllByIdTabouIsNullAndNomIsLikeIgnoreCase(String nom);

}
