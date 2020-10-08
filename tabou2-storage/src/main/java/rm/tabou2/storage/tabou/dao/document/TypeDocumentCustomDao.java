package rm.tabou2.storage.tabou.dao.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;



public interface TypeDocumentCustomDao {

    Page<TypeDocumentEntity> searchTypeDocument(TypeDocumentCriteria typeDocumentCriteria, Pageable pageable);

}
