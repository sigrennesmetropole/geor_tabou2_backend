package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeOccupation;
import rm.tabou2.service.helper.operation.TypeOccupationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.TypeOccupationMapper;
import rm.tabou2.service.tabou.operation.TypeOccupationService;
import rm.tabou2.storage.tabou.dao.operation.TypeOccupationCustomDao;
import rm.tabou2.storage.tabou.item.TypeOccupationCriteria;

@Service
public class TypeOccupationServiceImpl implements TypeOccupationService {

    @Autowired
    TypeOccupationMapper mapper;

    @Autowired
    TypeOccupationCustomDao typeOccupationCustomDao;

    @Autowired
    TypeOccupationRightsHelper rightsHelper;

    @Override
    public Page<TypeOccupation> searchTypeOccupations(TypeOccupationCriteria criteria, Pageable pageable) {
        if(!rightsHelper.checkCanAccess()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits pour rechercher un type d'occupation.");
        }
        return mapper.entitiesToDto(typeOccupationCustomDao.searchTypeOccupation(criteria, pageable), pageable);
    }
}
