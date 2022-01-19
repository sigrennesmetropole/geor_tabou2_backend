package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeContribution;
import rm.tabou2.storage.tabou.item.TypeContributionCriteria;

public interface TypeContributionService {
    Page<TypeContribution> searchTypesContributions(TypeContributionCriteria criteria, Pageable pageable);
}
