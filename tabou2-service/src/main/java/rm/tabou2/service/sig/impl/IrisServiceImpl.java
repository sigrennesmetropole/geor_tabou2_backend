package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
public class IrisServiceImpl implements IrisService {

    @Autowired
    private IrisDao irisDao;

    @Autowired
    private IrisCustomDao irisCustomDao;

    @Autowired
    private IrisMapper irisMapper;

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
