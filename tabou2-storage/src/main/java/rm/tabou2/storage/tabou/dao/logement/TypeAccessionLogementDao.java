package rm.tabou2.storage.tabou.dao.logement;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.logement.TypeAccessionLogementEntity;

public interface TypeAccessionLogementDao extends CustomCrudRepository<TypeAccessionLogementEntity, Long>, JpaRepository<TypeAccessionLogementEntity, Long>, TypeAccessionLogementCustomDao {
    TypeAccessionLogementEntity findByCode(String code);
}

