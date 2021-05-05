package rm.tabou2.service.tabou.evenement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.tabou.programme.EvenementProgrammeMapper;
import rm.tabou2.service.tabou.evenement.EvenementProgrammeService;
import rm.tabou2.storage.tabou.dao.evenement.EvenementProgrammeCustomDao;

@Service
public class EvenementProgrammeServiceImpl implements EvenementProgrammeService {

    @Autowired
    private EvenementProgrammeCustomDao evenementProgrammeCustomDao;

    @Autowired
    private EvenementProgrammeMapper evenementProgrammeMapper;

    @Override
    public Page<Evenement> searchEvenementsProgramme(long programmeId, Pageable pageable) {

        return evenementProgrammeMapper.entitiesToDto(evenementProgrammeCustomDao.searchEvenementsProgramme(programmeId,pageable), pageable);

    }

}
