package rm.tabou2.service.tabou.evenement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.tabou.operation.EvenementOperationMapper;
import rm.tabou2.service.tabou.evenement.EvenementOperationService;
import rm.tabou2.storage.tabou.dao.evenement.EvenementOperationCustomDao;

@Service
public class EvenementOperationServiceImpl implements EvenementOperationService {

    @Autowired
    private EvenementOperationCustomDao evenementOperationCustomDao;

    @Autowired
    private EvenementOperationMapper evenementOperationMapper;

    @Override
    public Page<Evenement> searchEvenementOperation(long operationId, Pageable pageable) {

        return evenementOperationMapper.entitiesToDto(evenementOperationCustomDao.searchEvenementsOperation(operationId, pageable), pageable);

    }

}
