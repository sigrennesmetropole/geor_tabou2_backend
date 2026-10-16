package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.RenouvellementEconomiquePlae;
import rm.tabou2.service.mapper.tabou.operation.RenouvellementEconomiquePlaeMapper;
import rm.tabou2.service.tabou.operation.RenouvellementEconomiquePlaeService;
import rm.tabou2.storage.tabou.dao.operation.RenouvellementEconomiquePlaeCustomDao;

@Service
@RequiredArgsConstructor
public class RenouvellementEconomiquePlaeServiceImpl implements RenouvellementEconomiquePlaeService {

    private final RenouvellementEconomiquePlaeCustomDao renouvellementEconomiquePlaeCustomDao;

    private final RenouvellementEconomiquePlaeMapper renouvellementEconomiquePlaeMapper;

    @Override
    public Page<RenouvellementEconomiquePlae> searchRenouvellementsEconomiquesPlae(Boolean actifUniquement, Pageable pageable) {
        return renouvellementEconomiquePlaeMapper.entitiesToDto(
                renouvellementEconomiquePlaeCustomDao.searchRenouvellementsEconomiquesPlae(actifUniquement, pageable), pageable);
    }
}
