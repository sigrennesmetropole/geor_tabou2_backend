package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.ConsommationEspace;
import rm.tabou2.service.mapper.tabou.operation.ConsommationEspaceMapper;
import rm.tabou2.service.tabou.operation.ConsommationEspaceService;
import rm.tabou2.storage.tabou.dao.operation.ConsommationEspaceDao;

import java.util.List;

@Service
public class ConsommationEspaceServiceImpl implements ConsommationEspaceService {

    @Autowired
    private ConsommationEspaceDao consommationEspaceDao;

    @Autowired
    private ConsommationEspaceMapper consommationEspaceMapper;

    @Override
    public List<ConsommationEspace> getAllConsommationsEspace() {
        return consommationEspaceMapper.entitiesToDto(consommationEspaceDao.findAll());
    }
}
