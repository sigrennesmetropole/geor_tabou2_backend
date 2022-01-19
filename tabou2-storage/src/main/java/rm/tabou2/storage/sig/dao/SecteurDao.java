package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.SecteurEntity;

import java.util.List;

public interface SecteurDao extends CustomCrudRepository<SecteurEntity, Integer>, JpaRepository<SecteurEntity, Integer> {

    List<SecteurEntity> findAllByIdTabouIsNullAndSecteurIsLikeIgnoreCase(String secteur, Pageable pageable);

    int countAllByIdTabouIsNullAndSecteurIsLikeIgnoreCase(String secteur);

}
