package rm.tabou2.storage.tabou.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.tabou.entity.operation.SecteurGeometryViewEntity;

public interface SecteurGeometryViewDao extends JpaRepository<SecteurGeometryViewEntity, Integer> {

    /**
     * Recherche d'un secteur par son id tabou.
     *
     * @param idTabou identifiant tabou du secteur
     * @return l'entité SecteurGeometryView correspondante
     */
    SecteurGeometryViewEntity findByIdTabou(int idTabou);
}

