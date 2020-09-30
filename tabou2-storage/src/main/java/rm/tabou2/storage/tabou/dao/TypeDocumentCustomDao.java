package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.OperationEntity;
import rm.tabou2.storage.tabou.entity.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import java.util.Date;


public interface TypeDocumentCustomDao {

    Page<TypeDocumentEntity> searchTypeDocument(Long id, String libelle, Date dateInactivite, Pageable pageable);

}
