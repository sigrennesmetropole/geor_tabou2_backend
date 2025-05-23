package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeAmenageur;
import rm.tabou2.service.helper.operation.TypeAmenageurRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.TypeAmenageurMapper;
import rm.tabou2.service.tabou.operation.TypeAmenageurService;
import rm.tabou2.storage.tabou.dao.operation.TypeAmenageurCustomDao;
import rm.tabou2.storage.tabou.item.TypeAmenageurCriteria;

@Service
@RequiredArgsConstructor
public class TypeAmenageurServiceImpl implements TypeAmenageurService {

    private final TypeAmenageurMapper mapper;

    private final TypeAmenageurCustomDao typeAmenageurCustomDao;

    private final TypeAmenageurRightsHelper typeAmenageurRightsHelper;

    @Override
    public Page<TypeAmenageur> searchTypesAmenageurs(TypeAmenageurCriteria criteria, Pageable pageable) {
        if(!typeAmenageurRightsHelper.checkCanAccess()){
            throw new AccessDeniedException("L'utilisateur n'a pas les droits pour accéder aux types aménageurs");
        }
        return mapper.entitiesToDto(typeAmenageurCustomDao.searchTypesAmenageurs(criteria, pageable), pageable);
    }
}
