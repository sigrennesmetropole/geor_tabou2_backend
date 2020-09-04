package rm.tabou2.service;

import rm.tabou2.service.dto.Commune;

import java.util.List;

public interface CommuneService {

    List<Commune> searchCommunes(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;


}
