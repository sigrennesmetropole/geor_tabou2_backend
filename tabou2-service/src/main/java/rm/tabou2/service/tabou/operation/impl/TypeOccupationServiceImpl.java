package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TypeOccupationServiceImpl implements TypeOccupationService {

    private final TypeOccupationMapper mapper;

    private final TypeOccupationCustomDao typeOccupationCustomDao;

    private final TypeOccupationRightsHelper rightsHelper;

    @Override
    public Page<TypeOccupation> searchTypeOccupations(TypeOccupationCriteria criteria, Pageable pageable) {
        if(!rightsHelper.checkCanAccess()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits pour rechercher un type d'occupation.");
        }
        return mapper.entitiesToDto(typeOccupationCustomDao.searchTypeOccupation(criteria, pageable), pageable);
    }
}
