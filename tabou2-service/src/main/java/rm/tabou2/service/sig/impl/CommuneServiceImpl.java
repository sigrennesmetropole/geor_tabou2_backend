package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.sig.CommuneService;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.mapper.sig.CommuneMapper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.dao.CommuneDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import java.util.List;

@Service
public class CommuneServiceImpl implements CommuneService {

    @Autowired
    private CommuneDao communeDao;

    @Autowired
    private CommuneMapper communeMapper;

    @Override
    public List<Commune> searchCommunes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        List<CommuneEntity> communes = communeDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return communeMapper.entitiesToDto(communes);

    }

}
