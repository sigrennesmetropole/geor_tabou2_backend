package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import rm.tabou2.service.OperationService;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.mapper.OperationMapper;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.dao.OperationDao;
import rm.tabou2.storage.tabou.entity.OperationEntity;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    private static final String DEFAULT_ORDER_BY = "createDate";

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public Operation addOperation(Operation operation) {

        OperationEntity operationEntity = operationMapper.dtoToEntity(operation);

        operationEntity.setCreateDate(new Date());
        operationEntity.setModifDate(new Date());
        operationEntity.setCreateUser(authentificationHelper.getConnectedUsername());
        operationEntity.setModifUser(authentificationHelper.getConnectedUsername());

        operationDao.save(operationEntity);

        return operationMapper.entityToDto(operationEntity);

    }

    @Override
    public List<Operation> getAllOperations(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        //Initialisation des variables
        orderBy = (orderBy == null) ? DEFAULT_ORDER_BY : orderBy;
        keyword = (keyword == null) ? "%" : "%" + keyword + "%";

        List<OperationEntity> entites = operationDao.findByKeyword(keyword, PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc));

        return operationMapper.entitiesToDto(entites);

    }

    @Override
    public List<Operation> searchOperation(String nom, String nature, String etape, Boolean diffusionRestreinte, String code, String numAds, LocalDate autorisationDateDebut, LocalDate autorisationDateFin,
                                           LocalDate operationnelDateDebut, LocalDate operationnelDateFin, LocalDate clotureDateDebut, LocalDate clotureDateFin,
                                           Integer start, Integer resultsNumber, String orderBy, Boolean asc) {


        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc);

        //Remplacement des champs non saisies par %
        nom = nom.equals("") ? "%" : nom;

        //TODO : ajouter les paramères de date operationnelDateDebut, operationnelDateFin, cloture dateDebut et clotureDateFin à la requête dans le DAO

        //TODO : convertir les localDate en date, ou bien vérifier si swagger peut gérérer en entrée de l'api des date au lieu des localDate
        List<OperationEntity> operations = operationDao.search(nom, nature, etape, code, numAds, autorisationDateDebut, autorisationDateFin, pageable);

        return (operationMapper.entitiesToDto(operations));

    }

    @Override
    public Operation getOperationById(long operationId) {

        Optional<OperationEntity> operationOpt = operationDao.findById(operationId);

        if (operationOpt.isEmpty()) {
            throw new NoSuchElementException("L'operation demandée n'existe pas, id=" + operationId);
        }

        return (operationMapper.entityToDto(operationOpt.get()));

    }



}
