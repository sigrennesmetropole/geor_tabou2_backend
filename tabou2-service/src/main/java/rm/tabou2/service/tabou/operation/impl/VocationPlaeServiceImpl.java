package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.VocationPlae;
import rm.tabou2.service.mapper.tabou.operation.VocationPlaeMapper;
import rm.tabou2.service.tabou.operation.VocationPlaeService;
import rm.tabou2.storage.tabou.dao.operation.VocationPlaeCustomDao;

@Service
@RequiredArgsConstructor
public class VocationPlaeServiceImpl implements VocationPlaeService {

    private final VocationPlaeCustomDao vocationPlaeCustomDao;

    private final VocationPlaeMapper vocationPlaeMapper;

    @Override
    public Page<VocationPlae> searchVocationsPlae(Boolean actifUniquement, Pageable pageable) {
        return vocationPlaeMapper.entitiesToDto(vocationPlaeCustomDao.searchVocationsPlae(actifUniquement, pageable), pageable);
    }
}
