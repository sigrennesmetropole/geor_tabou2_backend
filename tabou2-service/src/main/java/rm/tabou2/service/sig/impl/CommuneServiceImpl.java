package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.mapper.sig.CommuneMapper;
import rm.tabou2.service.sig.CommuneService;
import rm.tabou2.storage.sig.dao.CommuneCustomDao;
import rm.tabou2.storage.sig.dao.CommuneDao;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import java.util.NoSuchElementException;

/**
 * Service des communes
 */
@Service
public class CommuneServiceImpl implements CommuneService {

    @Autowired
    private CommuneDao communeDao;

    @Autowired
    private CommuneCustomDao communeCustomDao;

    @Autowired
    private CommuneMapper communeMapper;

    @Override
    public Commune getCommuneById(int communeId) {

        CommuneEntity communeEntity = communeDao.findOneById(communeId);

        //Si commune RM, on lève une exception 404
        if (communeEntity.getCommuneAgglo() == 0) {
            throw new NoSuchElementException("La commune demandée ne fait pas partie de Rennes Metropole");
        }

        return communeMapper.entityToDto(communeEntity);

    }


    @Override
    public Page<Commune> searchCommunes(String nom, Integer codeInsee, Pageable pageable) {

        return communeMapper.entitiesToDto(communeCustomDao.searchCommunes(nom, codeInsee, pageable), pageable);

    }

}
