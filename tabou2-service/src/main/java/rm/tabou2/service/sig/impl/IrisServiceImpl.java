package rm.tabou2.service.sig.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.service.mapper.sig.IrisMapper;
import rm.tabou2.service.sig.IrisService;
import rm.tabou2.storage.sig.dao.IrisCustomDao;
import rm.tabou2.storage.sig.dao.IrisDao;
import rm.tabou2.storage.sig.entity.IrisEntity;
import rm.tabou2.storage.sig.item.IrisCriteria;

@Service
@RequiredArgsConstructor
public class IrisServiceImpl implements IrisService {

    private final IrisDao irisDao;

    private final IrisCustomDao irisCustomDao;

    private final IrisMapper irisMapper;

    @Override
    public Page<Iris> searchIris(IrisCriteria irisCriteria, Pageable pageable) {

        return irisMapper.entitiesToDto(irisCustomDao.searchIris(irisCriteria, pageable), pageable);

    }

    @Override
    public Iris getIrisById(int irisId) {

        IrisEntity irisEntity = irisDao.findOneById(irisId);

        return irisMapper.entityToDto(irisEntity);

    }

}
