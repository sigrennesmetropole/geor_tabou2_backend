package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.EntiteReferente;
import rm.tabou2.service.helper.operation.EntiteReferenteRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.EntiteReferenteMapper;
import rm.tabou2.service.tabou.operation.EntiteReferenteService;
import rm.tabou2.storage.tabou.dao.operation.EntiteReferenteCustomDao;
import rm.tabou2.storage.tabou.item.EntiteReferenteCriteria;

@Service
@RequiredArgsConstructor
public class EntiteReferenteServiceImpl implements EntiteReferenteService {

    private final EntiteReferenteCustomDao entiteReferenteCustomDao;

    private final EntiteReferenteMapper mapper;

    private final EntiteReferenteRightsHelper rightsHelper;

    @Override
    public Page<EntiteReferente> searchEntitesReferentes(EntiteReferenteCriteria criteria, Pageable pageable) {
        if(!rightsHelper.checkCanAccess()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de rechercher une entité Référente.");
        }
        return mapper.entitiesToDto(entiteReferenteCustomDao.searchEntitesReferentes(criteria, pageable), pageable);
    }
}
