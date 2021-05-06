package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.mapper.tabou.operation.DecisionMapper;
import rm.tabou2.service.tabou.operation.DecisionService;
import rm.tabou2.storage.tabou.dao.operation.DecisionCustomDao;

@Service
public class DecisionServiceImpl implements DecisionService {

    @Autowired
    private DecisionCustomDao decisionCustomDao;

    @Autowired
    private DecisionMapper decisionMapper;

    @Override
    public Page<Decision> searchDecisions(Pageable pageable) {
        return decisionMapper.entitiesToDto(decisionCustomDao.searchDecisions(pageable), pageable);
    }
}
