package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeFinancementOperation;
import rm.tabou2.storage.tabou.item.TypeFinancementOperationCriteria;

public interface TypeFinancementOperationService {

    Page<TypeFinancementOperation> searchTypesFinancementsOperations(TypeFinancementOperationCriteria criteria,
                                                                     Pageable pageable);
}
