package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.CommuneService;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.mapper.CommuneMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.CommuneDao;
import rm.tabou2.storage.tabou.entity.administratif.CommuneEntity;

import java.util.List;

@Service
public class CommuneServiceImpl implements CommuneService {

    @Autowired
    private CommuneDao communeDao;

    @Autowired
    private CommuneMapper communeMapper;

    @Override
    public List<Commune> searchCommunes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<CommuneEntity> communes = communeDao.findByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));

        return communeMapper.entitiesToDto(communes);

    }

}
