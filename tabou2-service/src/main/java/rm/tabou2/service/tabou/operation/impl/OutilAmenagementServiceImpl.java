package rm.tabou2.service.tabou.operation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.OutilAmenagement;
import rm.tabou2.service.mapper.tabou.operation.OutilAmenagementMapper;
import rm.tabou2.service.tabou.operation.OutilAmenagementService;
import rm.tabou2.storage.tabou.dao.operation.OutilAmenagementCustomDao;

@Service
@RequiredArgsConstructor
public class OutilAmenagementServiceImpl implements OutilAmenagementService {

    private final OutilAmenagementCustomDao outilAmenagementCustomDao;

    private final OutilAmenagementMapper outilAmenagementMapper;

    @Override
    public Page<OutilAmenagement> searchOutilsAmenagement(Pageable pageable) {
        return outilAmenagementMapper.entitiesToDto(outilAmenagementCustomDao.searchOutilsAmenagement(pageable), pageable);
    }

}
