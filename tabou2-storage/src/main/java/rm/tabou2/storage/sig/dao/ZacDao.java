package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.ZacEntity;

import java.util.List;

public interface ZacDao extends CustomCrudRepository<ZacEntity, Integer>, JpaRepository<ZacEntity, Integer> {

    List<ZacEntity> findAllByIdTabouIsNullAndNomZacIsLikeIgnoreCase(String nom, Pageable pageable);

    ZacEntity getByIdTabou(Integer idTabou);

    int countAllByIdTabouIsNullAndNomZacIsLikeIgnoreCase(String nom);

}
