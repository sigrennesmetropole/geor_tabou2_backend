package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.operation.TypeFinancementOperationEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementOperationCriteria;

public interface TypeFinancementOperationCustomDao {

    Page<TypeFinancementOperationEntity> searchTypesFinancementsOperations(TypeFinancementOperationCriteria criteria,
                                                                           Pageable pageable);
}
