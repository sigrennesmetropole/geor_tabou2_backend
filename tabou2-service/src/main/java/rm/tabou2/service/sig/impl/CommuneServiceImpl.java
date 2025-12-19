package rm.tabou2.service.sig.impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CommuneServiceImpl implements CommuneService {

    private final CommuneDao communeDao;

    private final CommuneCustomDao communeCustomDao;

    private final CommuneMapper communeMapper;

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
