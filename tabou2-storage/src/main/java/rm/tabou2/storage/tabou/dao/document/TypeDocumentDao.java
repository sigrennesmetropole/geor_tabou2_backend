package rm.tabou2.storage.tabou.dao.document;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;

import java.util.List;

public interface TypeDocumentDao extends CustomCrudRepository<TypeDocumentEntity, Long>, JpaRepository<TypeDocumentEntity, Long> {


    @Query("SELECT t FROM TypeDocumentEntity t WHERE UPPER(t.libelle) like UPPER(:keyword)")
    List<TypeDocumentEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM TypeDocumentEntity t WHERE UPPER(t.libelle) like UPPER(:keyword) and t.dateInactif is null")
    List<TypeDocumentEntity> findOnlyActiveByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
