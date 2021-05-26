package rm.tabou2.service.helper.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EtapeOperationWorkflowHelper {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private EtapeOperationMapper etapeOperationMapper;

    /**
     * Liste des étapes possible pour une opération
     * @param idOperation   identifiant de l'opération
     * @return              étapes
     */
    public List<Etape> getAccessibleEtapesForOperation(Long idOperation) {
        OperationEntity operationEntity = operationDao.findOneById(idOperation);
        List<EtapeOperationEntity> nextEtapes = List.copyOf(operationEntity.getEtapeOperation().getNextEtapes());
        return etapeOperationMapper.entitiesToDto(nextEtapes);
    }

    /**
     * Permet de savoir si on peut affecter une étape à une opération
     * @param newEtape      etape à affecter
     * @param idOperation   identifiant de l'opération
     * @return              true si on peut affectuer newEtape à l'opération
     */
    public boolean checkCanAssignEtapeToOperation(Etape newEtape, Long idOperation) {
        OperationEntity operationEntity = operationDao.findOneById(idOperation);
        EtapeOperationEntity actualEtapeEntity = operationEntity.getEtapeOperation();
        if (actualEtapeEntity.getCode().equals(newEtape.getCode())) {
            return true;
        }
        List<EtapeOperationEntity> nextEtapesEntities = List.copyOf(actualEtapeEntity.getNextEtapes());
        List<Etape> nextEtapes = etapeOperationMapper.entitiesToDto(nextEtapesEntities);
        List<Long> nextEtapesIdList = nextEtapes.stream().map(Etape::getId).collect(Collectors.toList());

        return nextEtapesIdList.contains(newEtape.getId());
    }
}
