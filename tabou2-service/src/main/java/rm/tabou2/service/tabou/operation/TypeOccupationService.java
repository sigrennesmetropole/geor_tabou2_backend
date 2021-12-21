package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeOccupation;
import rm.tabou2.storage.tabou.item.TypeOccupationCriteria;

public interface TypeOccupationService {
    Page<TypeOccupation> searchTypeOccupations(TypeOccupationCriteria criteria, Pageable pageable);
}
