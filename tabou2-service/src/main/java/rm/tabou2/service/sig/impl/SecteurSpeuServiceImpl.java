package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.service.mapper.sig.SecteurSpeuMapper;
import rm.tabou2.service.sig.SecteurSpeuService;
import rm.tabou2.storage.sig.dao.SecteurSpeuCustomDao;

@Service
public class SecteurSpeuServiceImpl implements SecteurSpeuService {

    @Autowired
    private SecteurSpeuCustomDao secteurSpeuCustomDao;

    @Autowired
    private SecteurSpeuMapper secteurSpeuMapper;

    @Override
    public Page<SecteurSpeu> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable) {

        return secteurSpeuMapper.entitiesToDto(secteurSpeuCustomDao.searchSecteursSpeu(numSecteur, nomSecteur, pageable), pageable);

    }

}
