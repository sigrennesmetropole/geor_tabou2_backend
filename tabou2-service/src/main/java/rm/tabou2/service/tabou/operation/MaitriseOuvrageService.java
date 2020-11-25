package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.MaitriseOuvrage;

import java.util.List;

public interface MaitriseOuvrageService {

    /**
     * Retourne la liste des maitrises d'ouvrage
     *
     * @return liste des maitrises d'ouvrage
     */
    List<MaitriseOuvrage> getAllMaitrisesOuvrage();
}
