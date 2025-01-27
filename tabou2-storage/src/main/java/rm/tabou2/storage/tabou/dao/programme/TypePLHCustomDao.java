package rm.tabou2.storage.tabou.dao.programme;

import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

public interface TypePLHCustomDao {

    /**
     * Recherche du parent d'un fils à partir de l'id du fils
     * @param filsId       id du fils
     * @return le parent du fils sinon null
     */
    TypePLHEntity getParentById(long filsId);
}
