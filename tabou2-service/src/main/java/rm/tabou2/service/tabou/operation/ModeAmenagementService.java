package rm.tabou2.service.tabou.operation;

import rm.tabou2.service.dto.ModeAmenagement;

import java.util.List;

public interface ModeAmenagementService {

    /**
     * Retourne la liste des modes d'aménagement
     *
     * @return liste des modes d'aménagement
     */
    List<ModeAmenagement> getAllModesAmenagement();
}
