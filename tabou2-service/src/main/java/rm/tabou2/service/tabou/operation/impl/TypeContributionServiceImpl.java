package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeContribution;
import rm.tabou2.service.mapper.tabou.operation.TypeContributionMapper;
import rm.tabou2.service.tabou.operation.TypeContributionService;
import rm.tabou2.storage.tabou.dao.operation.TypeContributionCustomDao;
import rm.tabou2.storage.tabou.item.TypeContributionCriteria;

@Service
@RequiredArgsConstructor
public class TypeContributionServiceImpl implements TypeContributionService {

    private final TypeContributionMapper mapper;

    private final TypeContributionCustomDao customDao;

    @Override
    public Page<TypeContribution> searchTypesContributions(TypeContributionCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(customDao.searchTypesContributions(criteria, pageable), pageable);
    }
}
