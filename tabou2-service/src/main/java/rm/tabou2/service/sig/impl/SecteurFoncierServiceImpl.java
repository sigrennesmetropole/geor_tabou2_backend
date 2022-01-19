package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.SecteurFoncier;
import rm.tabou2.service.mapper.sig.SecteurFoncierMapper;
import rm.tabou2.service.sig.SecteurFoncierService;
import rm.tabou2.storage.sig.dao.SecteurFoncierCustomDao;

@Service
public class SecteurFoncierServiceImpl implements SecteurFoncierService {

    @Autowired
    private SecteurFoncierCustomDao secteurFoncierCustomDao;

    @Autowired
    private SecteurFoncierMapper secteurFoncierMapper;

    @Override
    public Page<SecteurFoncier> searchSecteursFonciers(String negociateur, Pageable pageable) {
        return secteurFoncierMapper.entitiesToDto(secteurFoncierCustomDao.searchSecteursFoncier(negociateur, pageable), pageable);
    }

}
