package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeFoncier;
import rm.tabou2.storage.tabou.item.TypeFoncierCriteria;


public interface TypeFoncierService {

    Page<TypeFoncier> searchTypesFonciers(TypeFoncierCriteria criteria, Pageable pageable);
}
