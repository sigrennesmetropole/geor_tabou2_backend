package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.OutilFoncier;
import rm.tabou2.service.helper.operation.OutilFoncierRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.OutilsFonciersMapper;
import rm.tabou2.service.tabou.operation.OutilsFonciersService;
import rm.tabou2.storage.tabou.dao.operation.OutilFoncierCustomDao;
import rm.tabou2.storage.tabou.item.OutilFoncierCriteria;


@Service
@RequiredArgsConstructor
public class OutilFoncierServiceImpl implements OutilsFonciersService {

    private final OutilsFonciersMapper mapper;

    private final OutilFoncierCustomDao outilFoncierCustomDao;

    private final OutilFoncierRightsHelper rightsHelper;

    @Override
    public Page<OutilFoncier> searchOutilsFonciers(OutilFoncierCriteria criteria, Pageable pageable) {
        if (!rightsHelper.checkCanAccess()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits pour rechercher un outil foncier");
        }
        return mapper.entitiesToDto(outilFoncierCustomDao.searchOutilsFonciers(criteria, pageable), pageable);
    }
}
