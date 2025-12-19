package rm.tabou2.service.sig.impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PluiServiceImpl implements PluiService {


    private final PluiCustomDao pluiCustomDao;

    private final PluiDao pluiDao;

    private final PluiMapper pluiMapper;

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
