package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.service.mapper.sig.SecteurDdsMapper;
import rm.tabou2.service.sig.SecteurDdsService;
import rm.tabou2.storage.sig.dao.SecteurDdsCustomDao;

@Service
public class SecteurDdsServiceImpl implements SecteurDdsService {


    @Autowired
    private SecteurDdsCustomDao secteurDdsCustomDao;

    @Autowired
    private SecteurDdsMapper secteurDdsMapper;

    @Override
    public Page<SecteurDds> searchSecteursDds(String secteur, Pageable pageable) {
        return secteurDdsMapper.entitiesToDto(secteurDdsCustomDao.searchSecteursDds(secteur, pageable), pageable);
    }



}
