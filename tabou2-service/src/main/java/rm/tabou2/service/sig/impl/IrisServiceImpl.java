package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.service.mapper.sig.IrisMapper;
import rm.tabou2.service.sig.IrisService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.sig.dao.IrisDao;
import rm.tabou2.storage.sig.entity.IrisEntity;

import java.util.List;

@Service
public class IrisServiceImpl implements IrisService {

    @Autowired
    private IrisDao irisDao;

    @Autowired
    private IrisMapper irisMapper;

    @Override
    public List<Iris> searchIris(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        List<IrisEntity> iris = irisDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, IrisEntity.class));

        return irisMapper.entitiesToDto(iris);

    }

}
