package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.mapper.tabou.operation.VocationMapper;
import rm.tabou2.service.tabou.operation.VocationService;
import rm.tabou2.storage.tabou.dao.operation.VocationCustomDao;

@Service
@RequiredArgsConstructor
public class VocationServiceImpl implements VocationService {

    private final VocationCustomDao vocationCustomDao;

    private final VocationMapper vocationMapper;

    @Override
    public Page<Vocation> searchVocations(Pageable pageable) {
        return vocationMapper.entitiesToDto(vocationCustomDao.searchVocation(pageable), pageable);
    }
}
