package rm.tabou2.service.tabou.operation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.mapper.tabou.operation.MaitriseOuvrageMapper;
import rm.tabou2.service.tabou.operation.MaitriseOuvrageService;
import rm.tabou2.storage.tabou.dao.operation.MaitriseOuvrageDao;

import java.util.List;

@Service
public class MaitriseOuvrageServiceImpl implements MaitriseOuvrageService {

    @Autowired
    private MaitriseOuvrageDao maitriseOuvrageDao;

    @Autowired
    private MaitriseOuvrageMapper maitriseOuvrageMapper;

    @Override
    public List<MaitriseOuvrage> getAllMaitrisesOuvrage() {
        return maitriseOuvrageMapper.entitiesToDto(maitriseOuvrageDao.findAll());
    }
}
