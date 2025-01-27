package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeActeur;
import rm.tabou2.service.mapper.tabou.operation.TypeActeurMapper;
import rm.tabou2.service.tabou.operation.TypeActeurService;
import rm.tabou2.storage.tabou.dao.operation.TypeActeurCustomDao;
import rm.tabou2.storage.tabou.item.TypeActeurCriteria;

@Service
@RequiredArgsConstructor
public class TypeActeurServiceImpl implements TypeActeurService {

    private final TypeActeurMapper mapper;

    private final TypeActeurCustomDao customDao;

    @Override
    public Page<TypeActeur> searchTypesActeurs(TypeActeurCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(customDao.searchTypesActeurs(criteria, pageable), pageable);
    }
}
