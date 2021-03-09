package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.CommuneEntity;

public interface CommuneCustomDao {

    /**
     * Recherche des communes à partir des paramètres.
     *
     * @param nom       nom de la commune
     * @param codeInsee code insee de la commune
     * @param pageable  paramètre lié à la pagination
     * @return Liste des communes correspondant à la recherche
     */
    Page<CommuneEntity> searchCommunes(String nom, String codeInsee, Pageable pageable);

}
