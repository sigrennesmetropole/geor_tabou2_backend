package rm.tabou2.service.sig.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.SecteurSam;
import rm.tabou2.service.mapper.sig.SecteurSamMapper;
import rm.tabou2.service.sig.SecteurSamService;
import rm.tabou2.storage.sig.dao.SecteurSamCustomDao;

@Service
@RequiredArgsConstructor
public class SecteurSamServiceImpl implements SecteurSamService {

    private final SecteurSamCustomDao secteurSamCustomDao;

    private final SecteurSamMapper secteurSamMapper;

    @Override
    public Page<SecteurSam> searchSecteursSam(String secteur, Pageable pageable) {
        return secteurSamMapper.entitiesToDto(secteurSamCustomDao.searchSecteursSam(secteur, pageable), pageable);
    }

}
