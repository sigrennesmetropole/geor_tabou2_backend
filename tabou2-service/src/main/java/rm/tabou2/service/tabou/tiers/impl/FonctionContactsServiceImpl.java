package rm.tabou2.service.tabou.tiers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.FonctionContacts;
import rm.tabou2.service.mapper.tabou.tiers.FonctionContactsMapper;
import rm.tabou2.service.tabou.tiers.FonctionContactsService;
import rm.tabou2.storage.tabou.dao.tiers.FonctionContactsCustomDao;
import rm.tabou2.storage.tabou.item.FonctionContactsCriteria;

@Service
@RequiredArgsConstructor
public class FonctionContactsServiceImpl implements FonctionContactsService {

    private final FonctionContactsMapper fonctionContactsMapper;

    private final FonctionContactsCustomDao fonctionContactsCustomDao;

    @Override
    public Page<FonctionContacts> searchFonctionContacts(FonctionContactsCriteria criteria, Pageable pageable) {

        return fonctionContactsMapper.entitiesToDto(fonctionContactsCustomDao.searchFonctionsContacts(criteria, pageable), pageable);
    }
}
