package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.CommuneEntity;

import java.util.List;

public interface CommuneCustomDao {

    /**
     * Recherche des communes à partir des paramètres.
     *
     * @param nom       nom de la commune
     * @param codeInsee code insee de la commune
     * @param pageable  paramètre lié à la pagination
     * @return Liste des communes correspondant à la recherche
     */
    Page<CommuneEntity> searchCommunes(String nom, Integer codeInsee, Pageable pageable);

    /**
     * Recherches des communes sur lesquelles a lieu une opération
     * @param operationId id de l'opération
     * @param estSecteur l'opération est un secteur
     * @param estZac l'opération est une ZAC
     * @return Liste des communes correspondant à la recherche
     */
    List<CommuneEntity> searchCommunesByOperationId(Long operationId, boolean estSecteur, boolean estZac);

    /**
     *
     */

}
