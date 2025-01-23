package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeFinancementOperation;
import rm.tabou2.service.helper.operation.TypeFinancementOperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.TypeFinancementOperationMapper;
import rm.tabou2.service.tabou.operation.TypeFinancementOperationService;
import rm.tabou2.storage.tabou.dao.operation.TypeFinancementOperationCustomDao;
import rm.tabou2.storage.tabou.item.TypeFinancementOperationCriteria;

@Service
@RequiredArgsConstructor
public class TypeFinancementOperationServiceImpl implements TypeFinancementOperationService {

    private final TypeFinancementOperationCustomDao customDao;

    private final TypeFinancementOperationMapper mapper;

    private final TypeFinancementOperationRightsHelper rightsHelper;

    @Override
    public Page<TypeFinancementOperation> searchTypesFinancementsOperations(TypeFinancementOperationCriteria criteria, Pageable pageable) {
        if(!rightsHelper.checkCanAccess()){
            throw new AccessDeniedException("L'utilisateur n'est pas autorisé à accéder aux types de financements des opérations");
        }
        return mapper.entitiesToDto(customDao.searchTypesFinancementsOperations(criteria, pageable), pageable);
    }
}
