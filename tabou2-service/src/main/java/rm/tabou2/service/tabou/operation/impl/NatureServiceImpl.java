package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.mapper.tabou.operation.NatureMapper;
import rm.tabou2.service.tabou.operation.NatureService;
import rm.tabou2.storage.tabou.dao.operation.NatureCustomDao;

@Service
@RequiredArgsConstructor
public class NatureServiceImpl implements NatureService {

    private final NatureCustomDao natureCustomDao;

    private final NatureMapper natureMapper;

    @Override
    public Page<Nature> searchNatures(Boolean onlyActive, Pageable pageable) {

        return natureMapper.entitiesToDto(natureCustomDao.searchNature(onlyActive, pageable), pageable);

    }

}
