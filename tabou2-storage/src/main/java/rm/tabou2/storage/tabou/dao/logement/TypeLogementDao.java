package rm.tabou2.storage.tabou.dao.logement;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.logement.TypeLogementEntity;

public interface TypeLogementDao extends CustomCrudRepository<TypeLogementEntity, Long>, JpaRepository<TypeLogementEntity, Long>, TypeLogementCustomDao {
    TypeLogementEntity findByCode(String code);
}

