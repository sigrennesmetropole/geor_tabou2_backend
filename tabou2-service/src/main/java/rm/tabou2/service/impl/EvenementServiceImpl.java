package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rm.tabou2.service.EvenementService;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.EvenementMapper;
import rm.tabou2.storage.tabou.dao.EvenementDao;

import javax.validation.Valid;
import java.util.List;

@Service
public class EvenementServiceImpl implements EvenementService {

    @Autowired
    private EvenementDao evenementDao;

    @Autowired
    private EvenementMapper evenementMapper;

    @Override
    public List<Evenement> getByOperationId(Long operationId) throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        return evenementMapper.entitiesToDto(evenementDao.findByOperationId(operationId, pageable));

    }

    public ResponseEntity<Etape> addEvenementB(@Valid Etape etape) throws Exception {

        //return new ResponseEntity<>(etapeOperationService.addEtapeOperation(etape), HttpStatus.OK);
        return  null;

    }


}
