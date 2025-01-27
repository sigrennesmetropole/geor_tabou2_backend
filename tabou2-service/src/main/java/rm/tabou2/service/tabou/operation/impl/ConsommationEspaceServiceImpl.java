package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.ConsommationEspace;
import rm.tabou2.service.mapper.tabou.operation.ConsommationEspaceMapper;
import rm.tabou2.service.tabou.operation.ConsommationEspaceService;
import rm.tabou2.storage.tabou.dao.operation.ConsommationEspaceCustomDao;

@Service
@RequiredArgsConstructor
public class ConsommationEspaceServiceImpl implements ConsommationEspaceService {

    private final ConsommationEspaceCustomDao consommationEspaceCustomDao;

    private final ConsommationEspaceMapper consommationEspaceMapper;

    @Override
    public Page<ConsommationEspace> searchConsommationsEspace(Pageable pageable) {
        return consommationEspaceMapper.entitiesToDto(consommationEspaceCustomDao.searchConsommationEspace(pageable), pageable);
    }
}
