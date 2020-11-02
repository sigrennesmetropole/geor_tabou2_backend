package rm.tabou2.service.helper.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import java.util.List;

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
        OperationEntity operationEntity = operationDao.getById(idOperation);
        if (operationEntity == null) {
            throw new IllegalArgumentException("L'identifiant de l'operation est invalide: aucune opération trouvée pour l'id = " + idOperation);
        }
        List<EtapeOperationEntity> nextEtapes = List.copyOf(operationEntity.getEtapeOperation().getNextEtapes());
        return etapeOperationMapper.entitiesToDto(nextEtapes);
    }
}
