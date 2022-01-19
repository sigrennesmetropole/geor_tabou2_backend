package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.mapper.sig.QuartierMapper;
import rm.tabou2.service.sig.QuartierService;
import rm.tabou2.storage.sig.dao.QuartierCustomDao;
import rm.tabou2.storage.sig.dao.QuartierDao;
import rm.tabou2.storage.sig.entity.QuartierEntity;
import rm.tabou2.storage.sig.item.QuartierCriteria;

@Service
public class QuartierServiceImpl implements QuartierService {

    @Autowired
    private QuartierDao quartierDao;

    @Autowired
    private QuartierCustomDao quartierCustomDao;

    @Autowired
    private QuartierMapper quartierMapper;

    @Override
    public Page<Quartier> searchQuartiers(QuartierCriteria quartierCriteria, Pageable pageable) {

        return quartierMapper.entitiesToDto(quartierCustomDao.searchQuartiers(quartierCriteria, pageable), pageable);

    }

    @Override
    public Quartier getQuartierById(int quartierId) {

        QuartierEntity quartierEntity = quartierDao.findOneById(quartierId);

        return quartierMapper.entityToDto(quartierEntity);

    }

}
