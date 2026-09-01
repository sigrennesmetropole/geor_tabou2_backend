package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.tabou.entity.operation.OperationGeometryViewEntity;

public interface OperationGeometryViewDao extends JpaRepository<OperationGeometryViewEntity, Integer> {

    /**
     * Recherche d'une opération par son id tabou.
     *
     * @param idTabou identifiant tabou de l'opération
     * @return l'entité OperationGeometryView correspondante
     */
    OperationGeometryViewEntity findByIdTabou(int idTabou);
}

