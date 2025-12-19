package rm.tabou2.service.sig.impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class QuartierServiceImpl implements QuartierService {

    private final QuartierDao quartierDao;

    private final QuartierCustomDao quartierCustomDao;

    private final QuartierMapper quartierMapper;

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
