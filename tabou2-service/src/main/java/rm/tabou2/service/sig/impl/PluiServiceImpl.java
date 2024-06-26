package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.service.mapper.sig.PluiMapper;
import rm.tabou2.service.sig.PluiService;
import rm.tabou2.storage.sig.dao.PluiCustomDao;
import rm.tabou2.storage.sig.dao.PluiDao;
import rm.tabou2.storage.sig.entity.PluiEntity;


@Service
public class PluiServiceImpl implements PluiService {


    @Autowired
    private PluiCustomDao pluiCustomDao;

    @Autowired
    private PluiDao pluiDao;

    @Autowired
    private PluiMapper pluiMapper;

    @Override
    public Page<PluiZonage> searchPlui(String libelle, Pageable pageable) {

        return pluiMapper.entitiesToDto(pluiCustomDao.searchPluis(libelle, pageable), pageable);

    }

    @Override
    public PluiZonage getPluiById(int pluiId) {

        PluiEntity pluiZonage = pluiDao.findOneById(pluiId);

        return pluiMapper.entityToDto(pluiZonage);

    }

}
