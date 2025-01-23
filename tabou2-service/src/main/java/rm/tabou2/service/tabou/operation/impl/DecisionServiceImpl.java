package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.mapper.tabou.operation.DecisionMapper;
import rm.tabou2.service.tabou.operation.DecisionService;
import rm.tabou2.storage.tabou.dao.operation.DecisionCustomDao;

@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {

    private final DecisionCustomDao decisionCustomDao;

    private final DecisionMapper decisionMapper;

    @Override
    public Page<Decision> searchDecisions(Pageable pageable) {
        return decisionMapper.entitiesToDto(decisionCustomDao.searchDecisions(pageable), pageable);
    }
}
