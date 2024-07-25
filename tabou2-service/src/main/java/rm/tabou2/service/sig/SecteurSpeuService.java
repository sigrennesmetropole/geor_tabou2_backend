package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;

public interface SecteurSpeuService {

    /**
     * Recherche de secteurs Speu à partir de paramètres.
     *
     * @param numSecteur numéro de secteur
     * @param nomSecteur nom de secteur
     * @param pageable pagination
     * @return liste de secteur Speu
     */
    Page<SecteurSpeu> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable);

    /**
     *  Génère la fiche de suivi d'une opération.
     * @param operationId identifiant de l'opération
     * @return La fiche de suivi
     * @throws AppServiceException exception
     */
    DocumentContent generateFicheSuivi(Long operationId) throws AppServiceException;

}
