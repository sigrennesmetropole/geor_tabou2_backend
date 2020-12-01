package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.mapper.tabou.operation.DecisionMapper;
import rm.tabou2.service.tabou.operation.DecisionService;
import rm.tabou2.storage.tabou.dao.operation.DecisionDao;

import java.util.List;

@Service
public class DecisionServiceImpl implements DecisionService {

    @Autowired
    private DecisionDao decisionDao;

    @Autowired
    private DecisionMapper decisionMapper;

    @Override
    public List<Decision> getAllDecisions() {
        return decisionMapper.entitiesToDto(decisionDao.findAll());
    }
}
