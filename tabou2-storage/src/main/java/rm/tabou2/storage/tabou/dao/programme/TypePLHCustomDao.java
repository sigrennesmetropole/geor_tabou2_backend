package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.item.TypePLHCriteria;

public interface TypePLHCustomDao {

    /**
     * Recherche du parent d'un fils à partir de l'id du fils
     * @param filsId       id du fils
     * @return le parent du fils sinon null
     */
    TypePLHEntity getParentById(long filsId);

    /**
     * Recherche d'un typePLH à partir d'un TypePLHCriteria
     * @param typePLHCriteria   filtre de recherche
     * @param pageable pagination
     * @return page des TypePlh trouvés
     */
    Page<TypePLHEntity> searchTypePLH(TypePLHCriteria typePLHCriteria, Pageable pageable);

}
