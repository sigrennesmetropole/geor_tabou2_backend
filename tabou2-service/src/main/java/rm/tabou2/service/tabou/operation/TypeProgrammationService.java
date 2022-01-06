package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeProgrammation;
import rm.tabou2.storage.tabou.item.TypeProgrammationCriteria;

public interface TypeProgrammationService {

    Page<TypeProgrammation> searchTypesProgrammations(TypeProgrammationCriteria criteria, Pageable pageable);
}
