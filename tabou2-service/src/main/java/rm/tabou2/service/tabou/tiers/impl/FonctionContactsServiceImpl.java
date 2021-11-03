package rm.tabou2.service.tabou.tiers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.FonctionContacts;
import rm.tabou2.service.mapper.tabou.tiers.FonctionContactsMapper;
import rm.tabou2.service.tabou.tiers.FonctionContactsService;
import rm.tabou2.storage.tabou.dao.tiers.FonctionContactsCustomDao;
import rm.tabou2.storage.tabou.item.FonctionContactsCriteria;

@Service
public class FonctionContactsServiceImpl implements FonctionContactsService {

    @Autowired
    FonctionContactsMapper fonctionContactsMapper;

    @Autowired
    FonctionContactsCustomDao fonctionContactsCustomDao;

    @Override
    public Page<FonctionContacts> searchFonctionContacts(FonctionContactsCriteria criteria, Pageable pageable) {

        return fonctionContactsMapper.entitiesToDto(fonctionContactsCustomDao.searchTiers(criteria, pageable), pageable);
    }
}
