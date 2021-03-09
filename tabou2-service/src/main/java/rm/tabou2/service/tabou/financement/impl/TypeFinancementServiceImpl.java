package rm.tabou2.service.tabou.financement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.TypeFinancement;
import rm.tabou2.service.mapper.tabou.financement.TypeFinancementMapper;
import rm.tabou2.service.tabou.financement.TypeFinancementService;
import rm.tabou2.storage.tabou.dao.financement.TypeFinancementCustomDao;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementCriteria;

@Service
@Validated
@Transactional(readOnly = true)
public class TypeFinancementServiceImpl implements TypeFinancementService {

    @Autowired
    TypeFinancementCustomDao typeFinancementCustomDao;

    @Autowired
    TypeFinancementMapper typeFinancementMapper;


    @Override
    public Page<TypeFinancement> searchTypeFinancement(TypeFinancementCriteria typeFinancementCriteria, Pageable pageable) {

        Page<TypeFinancementEntity> entities = typeFinancementCustomDao.searchTypeFinancement(typeFinancementCriteria, pageable);

        return typeFinancementMapper.entitiesToDto(entities, pageable);
    }
}
