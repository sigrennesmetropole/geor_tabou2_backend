package rm.tabou2.storage.tabou.dao.evenement;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

import java.util.List;

public interface TypeEvenementDao extends CustomCrudRepository<TypeEvenementEntity, Long>, JpaRepository<TypeEvenementEntity, Long> {

    @Query("SELECT t FROM TypeEvenementEntity t WHERE UPPER(t.libelle) like UPPER(:keyword)")
    List<TypeEvenementEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM TypeEvenementEntity t WHERE UPPER(t.libelle) like UPPER(:keyword) and t.dateInactif is null")
    List<TypeEvenementEntity> findOnlyActiveByKeyword(@Param("keyword") String keyword, Pageable pageable);

    TypeEvenementEntity findByCode(String code);
}
