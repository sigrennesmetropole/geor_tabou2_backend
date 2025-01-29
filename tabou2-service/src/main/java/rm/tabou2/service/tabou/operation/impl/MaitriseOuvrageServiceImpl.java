package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.mapper.tabou.operation.MaitriseOuvrageMapper;
import rm.tabou2.service.tabou.operation.MaitriseOuvrageService;
import rm.tabou2.storage.tabou.dao.operation.MaitriseOuvrageCustomDao;

@Service
@RequiredArgsConstructor
public class MaitriseOuvrageServiceImpl implements MaitriseOuvrageService {

    private final MaitriseOuvrageCustomDao maitriseOuvrageCustomDao;

    private final MaitriseOuvrageMapper maitriseOuvrageMapper;

    @Override
    public Page<MaitriseOuvrage> searchMaitrisesOuvrage(Pageable pageable) {
        return maitriseOuvrageMapper.entitiesToDto(maitriseOuvrageCustomDao.searchMaitriseOuvrage(pageable), pageable);
    }
}
