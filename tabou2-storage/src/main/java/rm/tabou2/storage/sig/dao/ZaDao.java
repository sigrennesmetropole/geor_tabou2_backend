package rm.tabou2.storage.sig.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.ZaEntity;

import java.util.List;

public interface ZaDao extends CustomCrudRepository<ZaEntity, Integer>, JpaRepository<ZaEntity, Integer> {

    List<ZaEntity> findAllByIdTabouIsNull();
}
